package wooteco.subway.path.domain;

import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.path.exception.PathException;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntPredicate;

public enum DistanceFare {

    NONE(DistanceFare::calculateFareDefault,
            distance -> distance > 0 && distance <= DistanceFare.DEFAULT_DISTANCE),
    DEFAULT(DistanceFare::calculateFareOverTen,
            distance -> distance > DistanceFare.DEFAULT_DISTANCE && distance <= DistanceFare.EXTRA_DISTANCE),
    EXTRA(DistanceFare::calculateFareOverFifty,
            distance -> distance > DistanceFare.EXTRA_DISTANCE);

    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_DISTANCE = 10;
    private static final int EXTRA_DISTANCE = 50;
    private static final int DEFAULT_INCREASE_MONEY = 5;
    private static final int EXTRA_INCREASE_MONEY = 8;

    private final Function<Integer, Integer> fareFunction;
    private final IntPredicate distancePredicate;

    DistanceFare(Function<Integer, Integer> fareFunction, IntPredicate distancePredicate) {
        this.fareFunction = fareFunction;
        this.distancePredicate = distancePredicate;
    }

    public static DistanceFare of(int distance) {
        return Arrays.stream(DistanceFare.values())
                .filter(item -> item.distancePredicate.test(distance))
                .findAny()
                .orElseThrow(() -> new SubwayCustomException(PathException.INVALID_DISTANCE_EXCEPTION));
    }

    public int calculateByDistance(int distance) {
        return fareFunction.apply(distance);
    }

    private static int calculateFareDefault(int distance) {
        return DEFAULT_FARE;
    }

    private static int calculateFareOverTen(int distance) {
        distance -= DEFAULT_DISTANCE;
        int beforeFare = calculateFareDefault(DEFAULT_DISTANCE);
        int fare = (int) ((Math.ceil((distance - 1) / DEFAULT_INCREASE_MONEY) + 1) * 100);
        return beforeFare + fare;
    }

    private static int calculateFareOverFifty(int distance) {
        distance -= EXTRA_DISTANCE;
        int beforeFare = calculateFareOverTen(EXTRA_DISTANCE);
        int fare = (int) ((Math.ceil((distance - 1) / EXTRA_INCREASE_MONEY) + 1) * 100);
        return beforeFare + fare;
    }
}
