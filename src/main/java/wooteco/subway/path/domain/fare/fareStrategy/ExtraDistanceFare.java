package wooteco.subway.path.domain.fare.fareStrategy;

public class ExtraDistanceFare implements DistanceFare {

    private static final int BASIC_DISTANCE = 10;
    private static final int EXTRA_DISTANCE_UNIT = 5;
    private static final int EXTRA_FARE_UNIT = 100;

    private final int rawFare;

    public ExtraDistanceFare(int distance) {
        int extraDistance = distance - BASIC_DISTANCE;
        BasicDistanceFare basicDistanceFare = new BasicDistanceFare(BASIC_DISTANCE);
        int extraFareUnit = (int) Math.ceil((double) extraDistance / EXTRA_DISTANCE_UNIT);
        int extraFare = extraFareUnit * EXTRA_FARE_UNIT;
        this.rawFare = basicDistanceFare.value() + extraFare;
    }

    @Override
    public int value() {
        return rawFare;
    }
}
