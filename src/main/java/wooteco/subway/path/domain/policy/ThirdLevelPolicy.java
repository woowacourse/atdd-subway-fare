package wooteco.subway.path.domain.policy;

public class ThirdLevelPolicy implements Policy {

    private static final int DIVIDE_UNIT = 8;
    private static final int MIN_LIMIT = 50;

    public ThirdLevelPolicy() {
    }

    @Override
    public void setNextLevelPolicy(Policy nextLevelPolicy) {
    }

    @Override
    public int calculateTotalFare(int distance) {
        return (int) ((Math.ceil((distance - MIN_LIMIT - 1) / DIVIDE_UNIT) + 1) * 100);
    }
}
