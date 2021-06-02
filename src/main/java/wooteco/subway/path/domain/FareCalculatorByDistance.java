package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.UnaryOperator;

public enum FareCalculatorByDistance {
    BASE(0, Constants.BASE_MAX_BOUNDARY, distance -> Constants.BASE_FARE),
    FIRST_ADDITIONAL(Constants.BASE_MAX_BOUNDARY, Constants.FIRST_ADDITIONAL_MAX_BOUNDARY,
            (distance) -> Constants.BASE_FARE + (int) ((Math.ceil((distance - Constants.BASE_MAX_BOUNDARY - 1) / 5)) + 1) * Constants.EXTRA_FARE),
    SECOND_ADDITIONAL(Constants.FIRST_ADDITIONAL_MAX_BOUNDARY, Integer.MAX_VALUE,
            (distance) -> Constants.BASE_FARE + 800 + (int) ((Math.ceil((distance - Constants.FIRST_ADDITIONAL_MAX_BOUNDARY - 1) / 8)) + 1) * Constants.EXTRA_FARE);


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

    private static class Constants {
        public static final int BASE_MAX_BOUNDARY = 10;
        public static final int FIRST_ADDITIONAL_MAX_BOUNDARY = 50;
        public static final int BASE_FARE = 1250;
        public static final int EXTRA_FARE = 100;
    }
}
