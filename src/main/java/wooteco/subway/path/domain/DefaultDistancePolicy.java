package wooteco.subway.path.domain;

public class DefaultDistancePolicy extends DistancePolicy {
    private static final int UNIT_DISTANCE = 0;
    private static final int EXTRA_DISTANCE = 0;
    private static final int BASIC_FARE = 1250;

    public DefaultDistancePolicy() {
        super(UNIT_DISTANCE, EXTRA_DISTANCE, BASIC_FARE);
    }

    @Override
    public int calculate(int distance) {
        return BASIC_FARE;
    }
}
