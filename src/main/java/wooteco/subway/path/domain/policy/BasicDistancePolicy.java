package wooteco.subway.path.domain.policy;

public class BasicDistancePolicy implements DistancePolicy {

    private static final int MAX_LIMIT = 10;
    private static final int BASIC_FARE = 1250;

    private DistancePolicy nextDistancePolicy;

    public BasicDistancePolicy() {
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

        return BASIC_FARE;
    }
}
