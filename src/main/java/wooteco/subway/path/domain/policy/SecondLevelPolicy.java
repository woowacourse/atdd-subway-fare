package wooteco.subway.path.domain.policy;

public class SecondLevelPolicy implements Policy {

    private static final int DIVIDE_UNIT = 5;
    private static final int MIN_LIMIT = 10;
    private static final int MAX_LIMIT = 50;

    private Policy nextLevelPolicy;

    public SecondLevelPolicy() {
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

        return (int) ((Math.ceil((distance - MIN_LIMIT - 1) / DIVIDE_UNIT) + 1) * 100);
    }
}
