package wooteco.subway.path.domain.farepolicy;

import java.math.BigDecimal;

public class DefaultFarePolicy implements FarePolicy {
    private static final int FARE = 1250;

    public BigDecimal calculate(int distance) {
        if (distance <= 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(FARE);
    }

}
