package wooteco.subway.path.domain.strategy.additional;

public class NoDiscount extends AgeDiscountPolicy {
    @Override
    protected double discountRate() {
        return 1;
    }
}
