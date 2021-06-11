package wooteco.subway.path.domain;

import java.util.Arrays;
import wooteco.subway.exception.InvalidDistanceException;

public enum DistancePolicy {

    BASIC(0, 0),
    SECOND(10, 5),
    THIRD(50, 8);

    private static final int DEFAULT_FARE = 1250;

    private final int minDistance;
    private final int divideUnit;

    DistancePolicy(int minDistance, int divideUnit) {
        this.minDistance = minDistance;
        this.divideUnit = divideUnit;
    }

    public static int calculateFare(int distance) {
        if (distance <= 10) {
            return DEFAULT_FARE;
        }

        return policyBy(distance).calculate(distance);
    }

    private static DistancePolicy policyBy(int distance) {
        return Arrays.stream(values())
            .filter(policy -> policy.minDistance < distance)
            .reduce((first, second) -> second)
            .orElseThrow(InvalidDistanceException::new);
    }

    private int calculate(int distance) {
        return calculateExtraFare(distance - minDistance) + calculateFare(minDistance);
    }

    private int calculateExtraFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / divideUnit) + 1) * 100);
    }
}
