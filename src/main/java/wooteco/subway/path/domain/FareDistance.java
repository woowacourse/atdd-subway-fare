package wooteco.subway.path.domain;

public interface FareDistance {
    int DEFAULT_FARE_DISTANCE = 10;
    int FIRST_EXTRA_FARE_DISTANCE_RANGE_END_DISTANCE = 50;
    int EXTRA_FARE_BY_DISTANCE = 100;

    static FareDistance of(int distance) {
        if (distance <= DEFAULT_FARE_DISTANCE) {
            return new DefaultFareDistance();
        }
        if (distance <= FIRST_EXTRA_FARE_DISTANCE_RANGE_END_DISTANCE) {
            return new FirstExtraFareDistance(distance);
        }
        return new SecondExtraFareDistance(distance);
    }

    Fare getFare(Fare currentFare);

    default Fare getExtraFare(int extraDistance, double distanceStandard) {
        double numberOfExtraStandardDistance = Math.ceil(extraDistance / distanceStandard);
        return new Fare(numberOfExtraStandardDistance * EXTRA_FARE_BY_DISTANCE);
    }
}
