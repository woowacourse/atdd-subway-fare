package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.IntPredicate;

public enum AgeAppliedRule {
    INFANT(0, 350, age -> (0 <= age) && (age < 6)),
    CHILD(0.5, 350, age -> (6 <= age) && (age < 13)),
    JUVENILE(0.80, 350, age -> (13 <= age) && (age < 19)),
    ADULT(1.00, 0, age -> (19 <= age));

    private final double appliedRate;
    private final int deductedFare;
    private final IntPredicate matchedFilter;

    AgeAppliedRule(double appliedRate, int deductedFare, IntPredicate matchedFilter) {
        this.appliedRate = appliedRate;
        this.deductedFare = deductedFare;
        this.matchedFilter = matchedFilter;
    }

    private static AgeAppliedRule matchedRule(int age) {
        return Arrays.stream(AgeAppliedRule.values())
            .filter(rule -> rule.matchedFilter.test(age))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("해당하는 나이가 존재하지 않습니다."));
    }

    public static int applyRule(int fare, int age) {
        return matchedRule(age).calculate(fare);
    }

    private int calculate(int fare) {
        return (int) ((fare - deductedFare) * appliedRate);
    }
}
