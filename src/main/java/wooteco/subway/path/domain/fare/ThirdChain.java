package wooteco.subway.path.domain.fare;

public class ThirdChain implements CalculateChain {
    private final static int SECOND_BOUND = 50;
    private final static double SECOND_DISTANCE_STANDARD = 8.0;
    private final static int EXTRA_FARE = 100;

    @Override
    public void setNextChain(CalculateChain calculateChain) {
    }

    @Override
    public int calculate(int distance) {
        return (int) (Math.ceil((distance - SECOND_BOUND)/ SECOND_DISTANCE_STANDARD) * EXTRA_FARE);
    }
}
