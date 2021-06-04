package wooteco.subway.path.domain.policy;

public class SecondDistancePolicy implements DistancePolicy {

    private static final int DIVIDE_UNIT = 5;
    private static final int MIN_LIMIT = 10;
    private static final int MAX_LIMIT = 50;

    private DistancePolicy nextDistancePolicy;

    public SecondDistancePolicy() {
    }

    @Override
    public void setNextPolicy(DistancePolicy nextDistancePolicy) {
        this.nextDistancePolicy = nextDistancePolicy;
    }

    @Override
    public int calculateFare(int distance) {
        if (MAX_LIMIT < distance) {
            return calculateFare(MAX_LIMIT) + nextDistancePolicy.calculateFare(distance);
        }

        return (int) ((Math.ceil((distance - MIN_LIMIT - 1) / DIVIDE_UNIT) + 1) * 100);
    }
}
