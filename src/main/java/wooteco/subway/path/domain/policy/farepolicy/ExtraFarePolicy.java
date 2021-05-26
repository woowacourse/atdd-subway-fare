package wooteco.subway.path.domain.policy.farepolicy;

import java.math.BigDecimal;

public interface ExtraFarePolicy {

    BigDecimal calculate(int distance);

}
