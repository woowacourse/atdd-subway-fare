package wooteco.subway.path.domain.strategy.additional;

public class InfancyDiscount extends AgeDiscountPolicy {
    public InfancyDiscount(int discountFare) {
        super(discountFare);
    }

    @Override
    public int calculateFare(int fare) {
        return 0;
    }

    @Override
    public boolean match(int age) {
        return age < 6;
    }
}
