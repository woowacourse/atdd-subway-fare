package wooteco.subway.path.domain;

public class SecondExtraFareDistance implements FareDistance {
    private static final double DISTANCE_STANDARD = 8.0;

    private final int distance;

    public SecondExtraFareDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public Fare getFare(Fare currentFare) {
        int extraDistance = distance - FIRST_EXTRA_FARE_DISTANCE_RANGE_END_DISTANCE;
        Fare extraFare = getExtraFare(extraDistance, DISTANCE_STANDARD);
        Fare fareBeforeRange = getBeforeDistanceFare();
        Fare extraTotalFare = fareBeforeRange.add(extraFare);
        return currentFare.add(extraTotalFare);
    }

    public Fare getBeforeDistanceFare() {
        return new FirstExtraFareDistance(FIRST_EXTRA_FARE_DISTANCE_RANGE_END_DISTANCE).getFullDistanceFareOfRange();
    }
}
