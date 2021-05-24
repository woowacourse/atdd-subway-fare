package wooteco.subway.path.domain.fare.distance;

import wooteco.subway.path.domain.fare.Fare;

public class DefaultFareDistance implements FareDistance {

    @Override
    public Fare getFare(Fare currentFare) {
        return currentFare;
    }
}
