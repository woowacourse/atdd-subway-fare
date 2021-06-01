package wooteco.subway.path.domain.fare;

public class FirstChain implements CalculateChain {
    private final static int DEFAULT_FARE = 1250;
    private final static int FIRST_BOUND = 10;

    private CalculateChain nextChain;

    @Override
    public void setNextChain(CalculateChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public int calculate(int distance) {
        if (distance > FIRST_BOUND) {
            return DEFAULT_FARE + nextChain.calculate(distance);
        }
        return DEFAULT_FARE;
    }
}
