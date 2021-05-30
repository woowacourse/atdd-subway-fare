package wooteco.subway.path.domain;

public class MiddleDistancePolicy extends DistancePolicy {
    private static final int UNIT_DISTANCE = 5;
    private static final int EXTRA_DISTANCE = 10;
    private static final int BASIC_FARE = 1250;

    public MiddleDistancePolicy() {
        super(UNIT_DISTANCE, EXTRA_DISTANCE, BASIC_FARE);
    }
}
