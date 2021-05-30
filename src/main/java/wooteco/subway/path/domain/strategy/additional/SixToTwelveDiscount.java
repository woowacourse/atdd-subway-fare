package wooteco.subway.path.domain.strategy.additional;

public class SixToTwelveDiscount extends AgeDiscountPolicy {
    public SixToTwelveDiscount(int discountFare) {
        super(discountFare);
    }

    @Override
    public int calculateFare(int fare) {
        return (int) ((fare - discountFare()) * 0.5);
    }
}
