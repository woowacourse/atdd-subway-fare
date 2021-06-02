package wooteco.subway.path.domain.fare.distance;

public class DefaultDistance implements DistanceChain {
    public static final int BASIC_FARE = 1250;

    private final DistanceChain chain;
    private final int threshold;

    public DefaultDistance(DistanceChain nextChain, int threshold) {
        this.chain = nextChain;
        this.threshold = threshold;
    }

    @Override
    public int calculate(int distance) {
        if (distance <= threshold) {
            return BASIC_FARE;
        }
        return BASIC_FARE + this.chain.calculate(distance - threshold);
    }
}
