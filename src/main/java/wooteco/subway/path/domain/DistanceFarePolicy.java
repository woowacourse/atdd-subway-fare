package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
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

    public static int distancePolicyAppliedFare(int originFare, int totalDistance) {
        Map<DistanceFarePolicy, Integer> distanceOfDistanceFarePolicy = distanceOfDistanceFarePolicy(
            totalDistance);

        for (Entry<DistanceFarePolicy, Integer> entry : distanceOfDistanceFarePolicy.entrySet()) {
            originFare += entry.getKey().calculateOverFare(entry.getValue());
        }
        return originFare;
    }

    private static Map<DistanceFarePolicy, Integer> distanceOfDistanceFarePolicy(
        int totalDistance) {
        Map<DistanceFarePolicy, Integer> distanceOfDistanceFarePolicy = new HashMap<>();
        for (int distance = 1; distance <= totalDistance; distance++) {
            putDistanceOfDistanceFarePolicy(distanceOfDistanceFarePolicy, distance);
        }
        return distanceOfDistanceFarePolicy;
    }

    private static void putDistanceOfDistanceFarePolicy(Map<DistanceFarePolicy, Integer> map,
        int distance) {
        Arrays.stream(DistanceFarePolicy.values())
            .filter(distanceFarePolicy -> distanceFarePolicy.test(distance))
            .forEach(distanceFarePolicy -> map
                .put(distanceFarePolicy, map.getOrDefault(distanceFarePolicy, 0) + 1));
    }

    private boolean test(int distance) {
        return policy.test(distance);
    }

    private int calculateOverFare(int distance) {
        if (distance == 0) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / range) + 1) * distancePerFare);
    }

}
