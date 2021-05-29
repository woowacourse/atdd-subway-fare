package wooteco.subway.path.domain.policy.extrafarepolicy;

import java.math.BigDecimal;

public interface ExtraFarePolicy {

    BigDecimal calculate(int distance);

}
