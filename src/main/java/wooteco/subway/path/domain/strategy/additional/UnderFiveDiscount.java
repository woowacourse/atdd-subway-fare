package wooteco.subway.path.domain.strategy.additional;

public class UnderFiveDiscount extends AgeDiscountPolicy {
    public UnderFiveDiscount(int discountFare) {
        super(discountFare);
    }

    @Override
    public int calculateFare(int fare) {
        return 0;
    }
}
