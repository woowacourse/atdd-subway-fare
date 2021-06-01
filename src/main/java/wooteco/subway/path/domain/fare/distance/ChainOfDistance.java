package wooteco.subway.path.domain.fare.distance;

public class ChainOfDistance {
    private static final int FIRST_THRESHOLD = 10;
    private static final int SECOND_THRESHOLD = 50;

    private final DistanceChain firstChain;

    public ChainOfDistance() {
        this.firstChain = new DefaultDistance(FIRST_THRESHOLD);
        DistanceChain secondChain = new SecondDistance(SECOND_THRESHOLD - FIRST_THRESHOLD);
        DistanceChain thirdChain = new ThirdDistance();

        firstChain.setNextChain(secondChain);
        secondChain.setNextChain(thirdChain);
    }

    public int calculate(int distance) {
        return firstChain.calculate(distance);
    }
}
