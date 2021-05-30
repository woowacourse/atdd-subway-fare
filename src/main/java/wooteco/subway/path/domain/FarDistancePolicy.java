package wooteco.subway.path.domain;

public class FarDistancePolicy extends DistancePolicy {
    private static final int UNIT_DISTANCE = 8;
    private static final int EXTRA_DISTANCE = 50;
    private static final int BASIC_FARE = 2250;

    public FarDistancePolicy() {
        super(UNIT_DISTANCE, EXTRA_DISTANCE, BASIC_FARE);
    }
}
