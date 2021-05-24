package wooteco.subway.path.domain.fare.distance;

import wooteco.subway.path.domain.fare.Fare;

public class FirstExtraFareDistance implements FareDistance {
    private static final double DISTANCE_STANDARD = 5.0;

    private final int distance;

    public FirstExtraFareDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public Fare getFare(Fare currentFare) {
        int extraDistance = distance - DEFAULT_FARE_DISTANCE;
        Fare extraFare = getExtraFare(extraDistance, DISTANCE_STANDARD);
        return currentFare.add(extraFare);
    }

    public Fare getFullDistanceFareOfRange() {
        return getFare(new Fare(0));
    }
}
