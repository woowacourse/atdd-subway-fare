package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.UnaryOperator;

public enum FareCalculatorByDistance {
    BASE_FARE(0, 10, distance -> 1250),
    FIRST_ADDITIONAL_FARE(10, 50,
            (distance) -> 1250 + (int) ((Math.ceil((distance - 10 - 1) / 5)) + 1) * 100),
    SECOND_ADDITIONAL_FARE(50, Integer.MAX_VALUE,
            (distance) -> 1250 + 800 + (int) ((Math.ceil((distance - 50 - 1) / 8)) + 1) * 100);

    private final int minExclusive;
    private final int maxInclusive;
    private final UnaryOperator<Integer> calculator;

    FareCalculatorByDistance(int minExclusive, int maxInclusive, UnaryOperator<Integer> calculator) {
        this.minExclusive = minExclusive;
        this.maxInclusive = maxInclusive;
        this.calculator = calculator;
    }

    public static int from(int distance) {
        return Arrays.stream(values())
                .filter(range -> range.minExclusive < distance && distance <= range.maxInclusive)
                .map(calculator -> calculator.calculator.apply(distance))
                .findAny()
                .orElse(0);
    }
}
