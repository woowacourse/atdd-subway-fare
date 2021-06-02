package wooteco.subway.path.domain.fare.distance;

public class ChainOfDistance {
    private static final int FIRST_THRESHOLD = 10;
    private static final int SECOND_THRESHOLD = 50;

    private final DistanceChain firstChain;

    public ChainOfDistance() {
        DistanceChain third = new ThirdDistance();
        DistanceChain second = new SecondDistance(third, SECOND_THRESHOLD - FIRST_THRESHOLD);
        this.firstChain = new DefaultDistance(second, FIRST_THRESHOLD);
    }

    public int calculate(int distance) {
        return firstChain.calculate(distance);
    }
}
