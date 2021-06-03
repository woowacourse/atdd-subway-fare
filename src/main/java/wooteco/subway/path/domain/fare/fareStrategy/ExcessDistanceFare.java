package wooteco.subway.path.domain.fare.fareStrategy;

public class ExcessDistanceFare implements DistanceFare {

    private static final int EXCESS_DISTANCE = 50;
    private static final int EXCESS_DISTANCE_UNIT = 8;
    private static final int EXCESS_FARE_UNIT = 100;

    private final int rawFare;

    public ExcessDistanceFare(int distance) {
        int excessDistance = distance - EXCESS_DISTANCE;
        ExtraDistanceFare extraDistanceFare = new ExtraDistanceFare(EXCESS_DISTANCE);
        int extraFareUnit = (int) Math.ceil((double) excessDistance / EXCESS_DISTANCE_UNIT);
        int excessFare = extraFareUnit * EXCESS_FARE_UNIT;
        this.rawFare = extraDistanceFare.value() + excessFare;
    }

    @Override
    public int value() {
        return this.rawFare;
    }
}
