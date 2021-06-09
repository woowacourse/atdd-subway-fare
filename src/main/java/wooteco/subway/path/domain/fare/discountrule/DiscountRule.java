package wooteco.subway.path.domain.fare.discountrule;

import wooteco.subway.path.domain.fare.Fare;

public interface DiscountRule {

    Fare apply(Fare fare);
}
