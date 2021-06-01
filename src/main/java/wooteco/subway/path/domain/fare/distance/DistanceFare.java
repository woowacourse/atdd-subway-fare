package wooteco.subway.path.domain.fare.distance;

import wooteco.subway.path.domain.fare.Fare;

public interface DistanceFare {
    int DEFAULT_FARE_DISTANCE = 10;
    int FIRST_EXTRA_FARE_DISTANCE_RANGE_END_DISTANCE = 50;
    int EXTRA_FARE_BY_DISTANCE = 100;

    Fare getFare(Fare currentFare);

    default Fare getExtraFare(int extraDistance, double distanceStandard) {
        double numberOfExtraStandardDistance = Math.ceil(extraDistance / distanceStandard);
        return new Fare(numberOfExtraStandardDistance * EXTRA_FARE_BY_DISTANCE);
    }
}
