package wooteco.subway.path.domain.strategy.additional;

public class ThirteenToNineTeenDiscount extends AgeDiscountPolicy {
    public ThirteenToNineTeenDiscount(int discountFare) {
        super(discountFare);
    }

    @Override
    public int calculateFare(int fare) {
        return (int) ((fare - discountFare()) * 0.8);
    }
}
