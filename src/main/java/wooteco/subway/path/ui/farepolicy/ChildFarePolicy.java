package wooteco.subway.path.ui.farepolicy;

public class ChildFarePolicy implements FarePolicy {
    private static final double discountRate = 0.5;

    public ChildFarePolicy() {
    }

    @Override
    public int discount(int fare) {
        return (int) Math.ceil((fare - deductible) * discountRate);
    }
}
