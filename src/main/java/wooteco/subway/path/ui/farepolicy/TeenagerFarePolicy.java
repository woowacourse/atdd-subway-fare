package wooteco.subway.path.ui.farepolicy;

public class TeenagerFarePolicy implements FarePolicy {
    private static final double discountRate = 0.8;

    public TeenagerFarePolicy() {
    }

    @Override
    public int discount(int fare) {
        return (int) Math.ceil((fare - deductible) * discountRate);
    }
}
