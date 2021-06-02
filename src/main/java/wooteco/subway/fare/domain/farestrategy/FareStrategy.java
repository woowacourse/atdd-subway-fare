package wooteco.subway.fare.domain.farestrategy;

import wooteco.subway.fare.domain.Money;

public interface FareStrategy {

    Money calculate(Money value);
}
