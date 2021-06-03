package wooteco.subway.path.domain.fare.fareStrategy;

public class BasicDistanceFare implements DistanceFare {

    private static final int BASIC_FARE = 1250;

    private final int rawFare;

    public BasicDistanceFare(int distance) {
        this.rawFare = BASIC_FARE;
    }

    @Override
    public int value() {
        return BASIC_FARE;
    }
}
