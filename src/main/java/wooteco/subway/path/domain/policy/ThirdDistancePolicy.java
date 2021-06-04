package wooteco.subway.path.domain.policy;

public class ThirdDistancePolicy implements DistancePolicy {

    private static final int DIVIDE_UNIT = 8;
    private static final int MIN_LIMIT = 50;

    public ThirdDistancePolicy() {
    }

    @Override
    public void setNextPolicy(DistancePolicy nextDistancePolicy) {
    }

    @Override
    public int calculateFare(int distance) {
        return (int) ((Math.ceil((distance - MIN_LIMIT - 1) / DIVIDE_UNIT) + 1) * 100);
    }
}
