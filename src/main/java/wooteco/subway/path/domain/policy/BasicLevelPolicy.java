package wooteco.subway.path.domain.policy;

public class BasicLevelPolicy implements Policy {

    private static final int MAX_LIMIT = 10;
    private static final int BASIC_FARE = 1250;

    private Policy nextLevelPolicy;

    public BasicLevelPolicy() {
    }

    @Override
    public void setNextLevelPolicy(Policy nextLevelPolicy) {
        this.nextLevelPolicy = nextLevelPolicy;
    }

    @Override
    public int calculateTotalFare(int distance) {
        if (MAX_LIMIT < distance) {
            return calculateTotalFare(MAX_LIMIT) + nextLevelPolicy.calculateTotalFare(distance);
        }

        return BASIC_FARE;
    }
}
