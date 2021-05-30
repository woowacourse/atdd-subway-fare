package wooteco.subway.path.domain.strategy.additional;

public class ThirteenToNineTeenDiscount extends AgeDiscountPolicy {
    @Override
    protected double discountRate() {
        return 0.8;
    }
}
