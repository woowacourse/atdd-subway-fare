package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.IntFunction;
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

    private static final int BASE_FARE = 1250;
    private static final int LESS_THAN_FIFTY_KM = 5;
    private static final int OVER_FIFTY_KM = 8;
    private static final int EXTRA_CHARGE_UNIT = 100;

    private final IntFunction<Integer> fareFunction;
    private final IntPredicate distancePredicate;

    FarePolicy(IntFunction<Integer> fareFunction, IntPredicate distancePredicate) {
        this.fareFunction = fareFunction;
        this.distancePredicate = distancePredicate;
    }

    public static FarePolicy of(int distance) {
        return Arrays.stream(FarePolicy.values())
            .filter(farePolicy -> farePolicy.distancePredicate.test(distance))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("해당 거리에 해당하는 요금 정책이 없습니다."));
    }

    private static int baseCalculate() {
        return BASE_FARE;
    }

    private static int overTenLessThanFiftyCalculate(int distance) {
        return baseCalculate() + calculateOverFare(distance - 10, LESS_THAN_FIFTY_KM);
    }

    private static int overFiftyCalculate(int distance) {
        return overTenLessThanFiftyCalculate(50) + calculateOverFare(distance - 50, OVER_FIFTY_KM);
    }

    private static int calculateOverFare(int distance, int km) {
        return ((distance - 1) / km + 1) * EXTRA_CHARGE_UNIT;
    }

    public int calculateFare(int distance) {
        return fareFunction.apply(distance);
    }
}
