package wooteco.subway.path.domain.fare.creationstrategy;

import wooteco.subway.path.domain.fare.Fare;

public interface FareCreationStrategy {

    Fare generate(int factor);
}
