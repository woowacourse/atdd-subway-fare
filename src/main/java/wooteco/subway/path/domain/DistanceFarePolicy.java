package wooteco.subway.path.domain;

import java.util.function.Predicate;

public enum DistanceFarePolicy {

    TEN_OVER_FIFTY_LESS_FARE_POLICY(distance -> distance > 10 && distance <= 50, 5, 100),
    FIFTY_OVER_FARE_POLICY(distance -> distance > 50, 8, 100);

    private final Predicate<Integer> policy;
    private final int range;
    private final int distancePerFare;

    DistanceFarePolicy(Predicate<Integer> policy, int range, int distancePerFare) {
        this.policy = policy;
        this.range = range;
        this.distancePerFare = distancePerFare;
    }

    public static int distancePolicyAppliedFare(int fare, int totalDistance) {
        for (DistanceFarePolicy distanceFarePolicy : DistanceFarePolicy.values()) {
            fare += distanceFarePolicy.calculateOverFare(totalDistance);
        }
        return fare;
    }

    public int calculateOverFare(int totalDistance) {
        int distance = 0;

        for (int i = 1; i <= totalDistance; i++) {
            if (policy.test(i)) {
                distance++;
            }
        }
        if (distance == 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / range) + 1) * distancePerFare);
    }

}
