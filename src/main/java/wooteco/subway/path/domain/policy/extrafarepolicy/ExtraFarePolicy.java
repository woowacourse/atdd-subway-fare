package wooteco.subway.path.domain.policy.extrafarepolicy;

import java.math.BigDecimal;

public interface ExtraFarePolicy {

    boolean isSatisfied(int distance);

    BigDecimal calculate(int distance);

}
