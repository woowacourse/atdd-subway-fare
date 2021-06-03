package wooteco.subway.path.domain.chain;

public class FirstBoundChain implements FareChain {
    private static final int BOUND = 10;
    private static final int DEFAULT_FARE = 1_250;

    private final FareChain chain;

    public FirstBoundChain(FareChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public int calculate(int distance) {
        if (distance <= BOUND) {
            return DEFAULT_FARE;
        }
        return DEFAULT_FARE + chain.calculate(distance);
    }
}
