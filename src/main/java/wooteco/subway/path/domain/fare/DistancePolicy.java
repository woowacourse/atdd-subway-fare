package wooteco.subway.path.domain.fare;

import java.util.Arrays;

public enum DistancePolicy {
    ADDITIONAL_LONG_DISTANCE(50, 8) {
        @Override
        public int calculate(final int distance, final int fare) {
            return ADDITIONAL_DISTANCE.calculate(ADDITIONAL_LONG_DISTANCE.distancePivot, fare)
                    + DistancePolicy.calculateAdditionalFareByDistance(
                    distance - ADDITIONAL_LONG_DISTANCE.distancePivot,
                    ADDITIONAL_LONG_DISTANCE.additionalPivot
            );
        }
    },
    ADDITIONAL_DISTANCE(10, 5) {
        @Override
        public int calculate(final int distance, final int fare) {
            return fare + FarePolicy.DEFAULT_FARE.getFare()
                    + DistancePolicy.calculateAdditionalFareByDistance(
                    distance - ADDITIONAL_DISTANCE.distancePivot, ADDITIONAL_DISTANCE.additionalPivot);
        }
    },

    DEFAULT_DISTANCE(0, 0) {
        @Override
        public int calculate(final int distance, final int fare) {
            return fare + FarePolicy.DEFAULT_FARE.getFare();
        }
    };

    private final int distancePivot;
    private final int additionalPivot;

    DistancePolicy(final int distancePivot, final int additionalPivot) {
        this.distancePivot = distancePivot;
        this.additionalPivot = additionalPivot;
    }

    public static int getFareApplyPolicy(final int distance, final int fare) {
        DistancePolicy policy = Arrays.stream(values())
                .filter(distancePolicy -> distancePolicy.distancePivot < distance)
                .findFirst()
                .orElse(DEFAULT_DISTANCE);

        return policy.calculate(distance, fare);
    }

    private static int calculateAdditionalFareByDistance(final int distance, final int pivot) {
        return (int) ((Math.ceil((distance - 1) / pivot) + 1) * FarePolicy.ADDITIONAL_FARE.getFare());
    }

    public abstract int calculate(final int distance, final int fare);
}
