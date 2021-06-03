package wooteco.subway.path.domain.chain;

public class ThirdBoundChain implements FareChain {
    private static final int BOUND = 50;
    private static final int EXTRA_FARE = 100;
    private static final int OVER_DISTANCE = 8;

    public ThirdBoundChain() {
    }

    @Override
    public int calculate(int distance) {
        return (int) ((Math.ceil(((distance - BOUND) - 1) / OVER_DISTANCE) + 1) * EXTRA_FARE);
    }
}
