package wooteco.subway.path.domain.fare.distance;

public class DefaultDistance implements DistanceChain {
    public static final int BASIC_FARE = 1250;

    private DistanceChain chain;
    private final int threshold;

    public DefaultDistance(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void setNextChain(DistanceChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public int calculate(int distance) {
        if (distance <= threshold) {
            return BASIC_FARE;
        }
        return BASIC_FARE + this.chain.calculate(distance - threshold);
    }
}
