package wooteco.subway.path.domain.fare.distance;

import wooteco.subway.path.domain.fare.Fare;

public class DistanceDefaultFare implements DistanceFare {

    @Override
    public Fare getFare(Fare currentFare) {
        return currentFare;
    }
}
