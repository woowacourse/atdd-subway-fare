package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

public enum FarePolicy {
    BASE_FARE(distance -> 1250, distance -> distance <= 10),
    OVER_TEN_LESS_THAN_FIFTY(
        distance -> 1250 + calculateOverFare(distance - 10, 5),
        distance -> distance > 10 && distance <= 50
    ),
    OVER_FIFTY(
        distance -> 1250 + calculateOverFare(40, 5) + calculateOverFare(distance - 50, 8),
        distance -> distance > 50
    );

    private IntFunction<Integer> fareFunction;
    private IntPredicate distancePredicate;

    FarePolicy(IntFunction<Integer> fareFunction, IntPredicate distancePredicate) {
        this.fareFunction = fareFunction;
        this.distancePredicate = distancePredicate;
    }

    private static int calculateOverFare(int distance, int km) {
        return (int) ((Math.ceil((distance - 1) / km) + 1) * 100);
    }

    public static FarePolicy of(int distance) {
        return Arrays.stream(FarePolicy.values())
            .filter(farePolicy -> farePolicy.distancePredicate.test(distance))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("해당 거리에 해당하는 요금 정책이 없습니다."));
    }

    public int calculateFare(int distance) {
        return fareFunction.apply(distance);
    }

}
