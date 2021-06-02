package wooteco.subway.path.domain;

import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.path.exception.PathException;

import java.util.Arrays;

public enum DistanceFare {

    NONE(0, 0) {
        @Override
        boolean isRangeDistance(int distance) {
            return distance > 0 && distance <= 10;
        }

        @Override
        int calculateFareByDistance(int distance) {
            return DEFAULT_FARE;
        }
    },
    DEFAULT(10, 5) {
        @Override
        boolean isRangeDistance(int distance) {
            return distance > 10 && distance <= 50;
        }

        @Override
        int calculateFareByDistance(int distance) {
            distance -= DEFAULT_DISTANCE;
            int beforeFare = NONE.calculateFareByDistance(distance);
            int fare = (int) ((Math.ceil((distance - 1) / DEFAULT.increaseMoneyPerDistance) + 1) * 100);
            return beforeFare + fare;
        }
    },
    EXTRA(50, 8) {
        @Override
        boolean isRangeDistance(int distance) {
            return distance > 50;
        }

        @Override
        int calculateFareByDistance(int distance) {
            distance -= EXTRA_DISTANCE;
            int beforeFare = DEFAULT.calculateFareByDistance(EXTRA_DISTANCE);
            int fare = (int) ((Math.ceil((distance - 1) / EXTRA.increaseMoneyPerDistance) + 1) * 100);
            return beforeFare + fare;
        }
    };

    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_DISTANCE = 10;
    private static final int EXTRA_DISTANCE = 50;

    private final int standardDistance;
    private final int increaseMoneyPerDistance;

    DistanceFare(int standardDistance, int increaseMoneyPerDistance) {
        this.standardDistance = standardDistance;
        this.increaseMoneyPerDistance = increaseMoneyPerDistance;
    }

    public static DistanceFare of(int distance) {
        return Arrays.stream(DistanceFare.values())
                .filter(item -> item.isRangeDistance(distance))
                .findAny()
                .orElseThrow(() -> new SubwayCustomException(PathException.INVALID_DISTANCE_EXCEPTION));
    }

    abstract boolean isRangeDistance(int distance);

    abstract int calculateFareByDistance(int distance);
}
