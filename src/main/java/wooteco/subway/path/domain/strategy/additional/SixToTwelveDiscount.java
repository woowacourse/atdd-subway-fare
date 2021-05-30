package wooteco.subway.path.domain.strategy.additional;

public class SixToTwelveDiscount extends AgeDiscountPolicy {
    @Override
    protected double discountRate() {
        return 0.5;
    }
}
