package wooteco.subway.path.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DistanceFareRule {
    BASIC_DISTANCE(distance -> false, 0, 1, 1250, 0),
    MIDDLE_DISTANCE(distance -> distance > 10 && distance <= 50, 10, 5, 1250, 100),
    LONG_DISTANCE(distance -> distance > 50, 50, 8, 2050, 100);

    private final Predicate<Integer> figureOutRule;
    private final int subtractFormulaValue;
    private final int divideFormulaValue;
    private final int defaultFare;
    private final int fareForDistanceFormulaValue;

    DistanceFareRule(Predicate<Integer> figureOutRule, int subtractFormulaValue,
                     int divideFormulaValue, int defaultFare, int fareForDistanceFormulaValue) {
        this.figureOutRule = figureOutRule;
        this.subtractFormulaValue = subtractFormulaValue;
        this.divideFormulaValue = divideFormulaValue;
        this.defaultFare = defaultFare;
        this.fareForDistanceFormulaValue = fareForDistanceFormulaValue;
    }

    public static int calculateFee(int distance) {
        DistanceFareRule findDistanceRule = findDistanceRule(distance);
        return additionalDistanceFee(findDistanceRule, distance);
    }

    public static DistanceFareRule findDistanceRule(int distance) {
        return Arrays.stream(DistanceFareRule.values())
                .filter(value -> value.figureOutRule.test(distance))
                .findAny()
                .orElse(BASIC_DISTANCE);
    }

    public static int additionalDistanceFee(DistanceFareRule rule, int distance) {
        return rule.defaultFare +
                (int) Math.ceil((double) (distance - rule.subtractFormulaValue) / rule.divideFormulaValue) * rule.fareForDistanceFormulaValue;
    }
}
