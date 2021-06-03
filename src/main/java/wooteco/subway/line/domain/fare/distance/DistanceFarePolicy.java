package wooteco.subway.line.domain.fare.distance;

import wooteco.subway.exception.InvalidDistanceException;

import java.util.Arrays;
import java.util.function.UnaryOperator;

import static wooteco.subway.line.domain.fare.distance.DefaultFare.DEFAULT_FARE_FIFTY_KILO;
import static wooteco.subway.line.domain.fare.distance.DefaultFare.DEFAULT_FARE_TEN_KILO;
import static wooteco.subway.line.domain.fare.distance.MaxDistance.*;

public enum DistanceFarePolicy {
    LESS_THAN_TEN_KILOMETER(TEN_KM,
            (distance) -> DEFAULT_FARE_TEN_KILO.value()),
    LESS_THAN_FIFTY_KILOMETER(FIFTY_KM,
            (distance) -> addFareByPerKilo(DEFAULT_FARE_TEN_KILO, distance - TEN_KM.value(), 5)),
    MORE_THAN_FIFTY_KILOMETER(OVER_FIFTY_KM,
            (distance) -> addFareByPerKilo(DEFAULT_FARE_FIFTY_KILO, distance - FIFTY_KM.value(), 8));

    private final MaxDistance maxDistance;
    private final UnaryOperator<Integer> operator;

    DistanceFarePolicy(MaxDistance maxDistance, UnaryOperator<Integer> operator) {
        this.maxDistance = maxDistance;
        this.operator = operator;
    }

    public static int calculate(int distance) {
        return findDistanceFarePolicy(distance).operator.apply(distance);
    }

    private static DistanceFarePolicy findDistanceFarePolicy(int distance) {
        return Arrays.stream(values())
                .filter(policy -> distance <= policy.maxDistance.value())
                .findAny()
                .orElseThrow(InvalidDistanceException::new);
    }

    private static int addFareByPerKilo(DefaultFare defaultFare, int distance, int perKiloMeter) {
        return defaultFare.value() + (((distance - 1) / perKiloMeter + 1) * 100);
    }
}
