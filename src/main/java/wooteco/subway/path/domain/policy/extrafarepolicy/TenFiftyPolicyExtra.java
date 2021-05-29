package wooteco.subway.path.domain.policy.extrafarepolicy;

import java.math.BigDecimal;

public class TenFiftyPolicyExtra implements ExtraFarePolicy {
    private static final int FARE = 100;
    private static final int OVER_FARE_DISTANCE = 5;
    private static final int DOWN_THRESHOLD = 10;
    private static final int UP_THRESHOLD = 50;

    @Override
    public BigDecimal calculate(int distance) {
        distance = initializedDistance(distance);

        if (distance <= 0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(calculateOverFare(distance));
    }

    private int initializedDistance(int distance) {
        distance -= DOWN_THRESHOLD;
        distance = Math.min(distance, UP_THRESHOLD - DOWN_THRESHOLD);
        return distance;
    }

    private int calculateOverFare(int distance) {
        return (int) Math.ceil(
            (double) ((distance - 1) / OVER_FARE_DISTANCE) + 1
        ) * FARE;
    }

}
