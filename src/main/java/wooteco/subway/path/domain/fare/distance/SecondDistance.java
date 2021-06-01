package wooteco.subway.path.domain.fare.distance;

public class SecondDistance implements DistanceChain {
    private static final int UNIT = 5;
    private static final int UNIT_FARE = 100;

    private DistanceChain chain;
    private final int threshold;

    public SecondDistance(int threshold) {
        this.threshold = threshold;
    }

    @Override
    public void setNextChain(DistanceChain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public int calculate(int distance) {
        if (distance > threshold) {
            return calculate(threshold) + this.chain.calculate(distance - threshold);
        }

        int count = (distance - 1) / UNIT;
        return (int) ((Math.ceil(count) + 1) * UNIT_FARE);
    }
}
