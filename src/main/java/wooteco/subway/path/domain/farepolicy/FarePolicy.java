package wooteco.subway.path.domain.farepolicy;

import java.math.BigDecimal;

public interface FarePolicy {

    BigDecimal calculate(int distance);
}
