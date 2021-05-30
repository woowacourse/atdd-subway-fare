package wooteco.subway.path.domain.strategy.additional;

public class UnderFiveDiscount extends AgeDiscountPolicy {
    @Override
    protected double discountRate() {
        return 0;
    }
}
