package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.UnaryOperator;

import static wooteco.subway.path.domain.FareCalculatorByDistance.Constants.*;

public enum FareCalculatorByDistance {
    BASE(0, FIRST_INTERVAL_START, distance -> BASE_FARE),
    FIRST_INTERVAL(FIRST_INTERVAL_START, SECOND_INTERVAL_START,
            (distance) -> BASE_FARE +
                    (int) ((Math.ceil((distance - FIRST_INTERVAL_START - 1) / FIRST_INTERVAL_CHARGE_UNIT)) + 1) * 100),
    SECOND_INTERVAL(SECOND_INTERVAL_START, Integer.MAX_VALUE,
            (distance) -> BASE_FARE + 800 +
                    (int) ((Math.ceil((distance - SECOND_INTERVAL_START - 1) / SECOND_INTERVAL_CHARGE_UNIT)) + 1) * 100);

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
                .orElseGet(() -> 0);
    }

    static class Constants {
        static final int BASE_FARE = 1250;
        static final int FIRST_INTERVAL_START = 10;
        static final int SECOND_INTERVAL_START = 50;
        static final int FIRST_INTERVAL_CHARGE_UNIT = 5;
        static final int SECOND_INTERVAL_CHARGE_UNIT = 8;
    }
}
