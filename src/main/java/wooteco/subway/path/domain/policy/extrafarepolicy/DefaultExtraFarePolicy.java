package wooteco.subway.path.domain.policy.extrafarepolicy;

import java.math.BigDecimal;

public class DefaultExtraFarePolicy implements ExtraFarePolicy {

    private static final int FARE = 1250;

    @Override
    public BigDecimal calculate(int distance) {
        if (distance <= 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(FARE);
    }

}
