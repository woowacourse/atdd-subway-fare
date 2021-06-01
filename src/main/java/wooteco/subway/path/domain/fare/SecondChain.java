package wooteco.subway.path.domain.fare;

public class SecondChain implements CalculateChain {
    private final static int FIRST_BOUND = 10;
    private final static int SECOND_BOUND = 50;
    private final static double FIRST_DISTANCE_STANDARD = 5.0;
    private final static int EXTRA_FARE = 100;

    private CalculateChain nextChain;

    @Override
    public void setNextChain(CalculateChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public int calculate(int distance) {
        if (distance > SECOND_BOUND) {
            return calculate(SECOND_BOUND) + nextChain.calculate(distance);
        }
        return (int) (Math.ceil((distance - FIRST_BOUND) / FIRST_DISTANCE_STANDARD) * EXTRA_FARE);
    }
}
