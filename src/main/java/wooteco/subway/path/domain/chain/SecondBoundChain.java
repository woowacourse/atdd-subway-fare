package wooteco.subway.path.domain.chain;

public class SecondBoundChain implements FareChain {
    private static final int PREVIOUS_BOUND = 10;
    private static final int BOUND = 50;
    private static final int EXTRA_FARE = 100;
    private static final int OVER_DISTANCE = 5;

    private final FareChain chain;

    public SecondBoundChain(FareChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public int calculate(int distance) {
        if (distance <= BOUND) {
            return (int) ((Math.ceil((distance - PREVIOUS_BOUND - 1) / OVER_DISTANCE) + 1) * EXTRA_FARE);
        }
        return calculate(BOUND - PREVIOUS_BOUND) + chain.calculate(distance);
    }
}
