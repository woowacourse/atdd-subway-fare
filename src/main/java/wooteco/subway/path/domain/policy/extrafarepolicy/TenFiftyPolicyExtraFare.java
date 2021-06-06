package wooteco.subway.path.domain.policy.extrafarepolicy;

import java.math.BigDecimal;

public class TenFiftyPolicyExtraFare extends DistanceExtraFarePolicy {

    private static final int FARE = 100;
    private static final int OVER_FARE_DISTANCE = 5;
    private static final int DOWN_THRESHOLD = 10;
    private static final int UP_THRESHOLD = 50;

    @Override
    public boolean isSatisfied(int distance) {
        return distance > DOWN_THRESHOLD;
    }

    @Override
    protected BigDecimal calculateExtraFare(int distance) {
        distance = initializedDistance(distance);

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
