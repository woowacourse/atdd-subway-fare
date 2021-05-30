package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntPredicate;

public enum DistanceFarePolicy {
    BASE(distance -> baseCalculate(),
            distance -> distance <= DistanceFarePolicy.TEN_KILO_METER
    ),
    OVER_TEN_LESS_THAN_FIFTY(
            DistanceFarePolicy::overTenLessThanFiftyCalculate,
            distance -> distance > DistanceFarePolicy.TEN_KILO_METER && distance <= DistanceFarePolicy.FIFTY_KILO_METER
    ),
    OVER_FIFTY(
            DistanceFarePolicy::overFiftyCalculate,
            distance -> distance > DistanceFarePolicy.FIFTY_KILO_METER
    );

    private static final Fare BASE_FARE = new Fare(1250);
    private static final int FIVE_KILO_METER = 5;
    private static final int EIGHT_KILO_METER = 8;
    private static final int TEN_KILO_METER = 10;
    private static final int FIFTY_KILO_METER = 50;
    private static final int EXTRA_CHARGE_UNIT = 100;

    private final Function<Integer, Fare> fareFunction;
    private final IntPredicate distancePredicate;

    DistanceFarePolicy(Function<Integer, Fare> fareFunction, IntPredicate distancePredicate) {
        this.fareFunction = fareFunction;
        this.distancePredicate = distancePredicate;
    }

    public static DistanceFarePolicy of(int distance) {
        return Arrays.stream(DistanceFarePolicy.values())
                .filter(distanceFarePolicy -> distanceFarePolicy.distancePredicate.test(distance))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 거리에 해당하는 요금 정책이 없습니다."));
    }

    public static Fare calculateFare(int distance){
        DistanceFarePolicy policy = DistanceFarePolicy.of(distance);
        return policy.fareFunction.apply(distance);
    }

    private static Fare baseCalculate() {
        return BASE_FARE;
    }

    private static Fare overTenLessThanFiftyCalculate(int distance) {
        return baseCalculate().add(calculateOverFare(distance - TEN_KILO_METER, FIVE_KILO_METER));
    }

    private static Fare overFiftyCalculate(int distance) {
        return overTenLessThanFiftyCalculate(FIFTY_KILO_METER).add(calculateOverFare(distance - FIFTY_KILO_METER, EIGHT_KILO_METER));
    }

    private static Fare calculateOverFare(int distance, int kmUnit) {
        return new Fare(((distance - 1) / kmUnit + 1) * EXTRA_CHARGE_UNIT);
    }
}
