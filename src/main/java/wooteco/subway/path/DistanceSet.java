package wooteco.subway.path;

import java.util.Arrays;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.line.exception.SectionExceptionSet;

public enum DistanceSet {

    DEFAULT_DISTANCE(10) {
        @Override
        boolean isContainDistance(int distance) {
            return distance <= getReferenceDistance();
        }

        @Override
        public int calculateDistanceFare(int distance) {
            return DEFAULT_FARE;
        }
    },
    LESS_THAN_FIFTY_DISTANCE(50) {
        @Override
        boolean isContainDistance(int distance) {
            return distance > DEFAULT_DISTANCE.getReferenceDistance()
                && distance <= getReferenceDistance();
        }

        @Override
        public int calculateDistanceFare(int distance) {
            distance -= DEFAULT_DISTANCE.getReferenceDistance();
            int beforeDistanceFare =
                DEFAULT_DISTANCE.calculateDistanceFare(DEFAULT_DISTANCE.getReferenceDistance());
            int fare = ((distance - 1) / TEN_TO_FIFTY_EXTRA_CHARGE_DISTANCE + 1) * EXTRA_CHARGE;

            return beforeDistanceFare + fare;
        }
    },

    FIFTY_OVER_DISTANCE(50) {
        @Override
        boolean isContainDistance(int distance) {
            return distance > getReferenceDistance();
        }

        @Override
        public int calculateDistanceFare(int distance) {
            distance -= LESS_THAN_FIFTY_DISTANCE.getReferenceDistance();
            int beforeDistanceFare = LESS_THAN_FIFTY_DISTANCE.calculateDistanceFare(
                LESS_THAN_FIFTY_DISTANCE.getReferenceDistance()
            );
            int fare = ((distance - 1) / OVER_FIFTY_EXTRA_CHARGE_DISTANCE + 1) * EXTRA_CHARGE;

            return beforeDistanceFare + fare;
        }
    };

    private static final int DEFAULT_FARE = 1250;
    private static final int EXTRA_CHARGE = 100;
    private static final int TEN_TO_FIFTY_EXTRA_CHARGE_DISTANCE = 5;
    private static final int OVER_FIFTY_EXTRA_CHARGE_DISTANCE = 8;

    private final int referenceDistance;

    DistanceSet(int referenceDistance) {
        this.referenceDistance = referenceDistance;
    }

    public static DistanceSet of(int distance) {
        return Arrays.stream(DistanceSet.values())
            .filter(item -> item.isContainDistance(distance))
            .findAny()
            .orElseThrow(() ->
                new SubwayException(SectionExceptionSet.INVALID_SECTION_DISTANCE_EXCEPTION)
            );
    }

    abstract boolean isContainDistance(int distance);

    public int getReferenceDistance() {
        return referenceDistance;
    }

    public abstract int calculateDistanceFare(int distance);
}
