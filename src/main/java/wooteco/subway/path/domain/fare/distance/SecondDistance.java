package wooteco.subway.path.domain.fare.distance;

public class SecondDistance implements DistanceChain {
    private static final int UNIT = 5;
    private static final int UNIT_FARE = 100;

    private final DistanceChain chain;
    private final int threshold;

    public SecondDistance(DistanceChain chain, int threshold) {
        this.chain = chain;
        this.threshold = threshold;
    }

    @Override
    public int calculate(int distance) {
        if (distance > threshold) {
            return calculate(threshold) + this.chain.calculate(distance - threshold);
        }
        return (int) ((Math.floor((distance - 1.) / UNIT) + 1) * UNIT_FARE);
    }
}
