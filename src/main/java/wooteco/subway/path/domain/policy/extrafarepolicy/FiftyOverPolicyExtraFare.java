package wooteco.subway.path.domain.policy.extrafarepolicy;

import java.math.BigDecimal;

public class FiftyOverPolicyExtraFare extends DistanceExtraFarePolicy {

    private static final int FARE = 100;
    private static final int OVER_FARE_DISTANCE = 8;
    private static final int THRESHOLD = 50;

    @Override
    public boolean isSatisfied(int distance) {
        return distance > THRESHOLD;
    }

    @Override
    protected BigDecimal calculateExtraFare(int distance) {
        distance -= THRESHOLD;

        return BigDecimal.valueOf(calculateOverFare(distance));
    }

    private int calculateOverFare(int distance) {
        return (int) Math.ceil(
            (double) ((distance - 1) / OVER_FARE_DISTANCE) + 1
        ) * FARE;
    }
}
