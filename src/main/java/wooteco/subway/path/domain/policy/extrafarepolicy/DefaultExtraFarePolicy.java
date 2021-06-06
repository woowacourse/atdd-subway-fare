package wooteco.subway.path.domain.policy.extrafarepolicy;

import java.math.BigDecimal;

public class DefaultExtraFarePolicy extends DistanceExtraFarePolicy {

    private static final BigDecimal DEFAULT_FARE = BigDecimal.valueOf(1250);
    private static final int THRESHOLD = 0;

    @Override
    public boolean isSatisfied(int distance) {
        return distance > THRESHOLD;
    }

    @Override
    protected BigDecimal calculateExtraFare(int distance) {
        return DEFAULT_FARE;
    }

}
