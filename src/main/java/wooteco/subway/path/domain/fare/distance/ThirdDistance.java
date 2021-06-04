package wooteco.subway.path.domain.fare.distance;

public class ThirdDistance implements DistanceChain {
    private static final int UNIT = 8;
    private static final int UNIT_FARE = 100;

    @Override
    public int calculate(int distance) {
        return (int) ((Math.floor((distance - 1.) / UNIT) + 1) * UNIT_FARE);
    }
}
