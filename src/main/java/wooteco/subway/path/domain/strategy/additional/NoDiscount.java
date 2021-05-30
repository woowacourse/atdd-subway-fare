package wooteco.subway.path.domain.strategy.additional;

public class NoDiscount extends AgeDiscountPolicy {
    public NoDiscount(int discountFare) {
        super(discountFare);
    }

    @Override
    public int calculateFare(int fare) {
        return fare;
    }
}
