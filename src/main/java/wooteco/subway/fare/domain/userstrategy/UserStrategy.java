package wooteco.subway.fare.domain.userstrategy;

import wooteco.subway.fare.domain.farestrategy.FarePolicy;

public interface UserStrategy {

    boolean isState();

    FarePolicy generateFarePolicy();
}
