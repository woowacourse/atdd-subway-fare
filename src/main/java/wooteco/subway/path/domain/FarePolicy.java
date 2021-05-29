package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntPredicate;

public enum FarePolicy {
    BASE(distance -> baseCalculate(),
            distance -> distance <= 10
    ),
    OVER_TEN_LESS_THAN_FIFTY(
            FarePolicy::overTenLessThanFiftyCalculate,
            distance -> distance > 10 && distance <= 50
    ),
    OVER_FIFTY(
            FarePolicy::overFiftyCalculate,
            distance -> distance > 50
    );

    private static final Fare BASE_FARE = new Fare(1250);
    private static final int FIVE_KILO_METER = 5;
    private static final int EIGHT_KILO_METER = 8;
    private static final int EXTRA_CHARGE_UNIT = 100;

    private final Function<Integer, Fare> fareFunction;
    private final IntPredicate distancePredicate;

    FarePolicy(Function<Integer, Fare> fareFunction, IntPredicate distancePredicate) {
        this.fareFunction = fareFunction;
        this.distancePredicate = distancePredicate;
    }

    public static FarePolicy of(int distance) {
        return Arrays.stream(FarePolicy.values())
                .filter(farePolicy -> farePolicy.distancePredicate.test(distance))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 거리에 해당하는 요금 정책이 없습니다."));
    }

    private static Fare baseCalculate() {
        return BASE_FARE;
    }

    private static Fare overTenLessThanFiftyCalculate(int distance) {
        return baseCalculate().add(calculateOverFare(distance - 10, FIVE_KILO_METER));
    }

    private static Fare overFiftyCalculate(int distance) {
        return overTenLessThanFiftyCalculate(50).add(calculateOverFare(distance - 50, EIGHT_KILO_METER));
    }

    private static Fare calculateOverFare(int distance, int kmUnit) {
        return new Fare(((distance - 1) / kmUnit + 1) * EXTRA_CHARGE_UNIT);
    }

    public Fare calculateFare(int distance) {
        return fareFunction.apply(distance);
    }
}
