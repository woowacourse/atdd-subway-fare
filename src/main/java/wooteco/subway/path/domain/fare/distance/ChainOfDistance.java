package wooteco.subway.path.domain.fare.distance;

public class ChainOfDistance {
    private static final int FIRST_THRESHOLD = 10;
    private static final int SECOND_THRESHOLD = 50;

    private final DistanceChain firstChain;

    public ChainOfDistance() {
        DistanceChain thirdChain = new ThirdDistance();
        DistanceChain secondChain = new SecondDistance(thirdChain, SECOND_THRESHOLD - FIRST_THRESHOLD);
        this.firstChain = new DefaultDistance(secondChain, FIRST_THRESHOLD);
    }

    public int calculate(int distance) {
        return firstChain.calculate(distance);
    }
}
