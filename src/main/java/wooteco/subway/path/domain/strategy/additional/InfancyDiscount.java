package wooteco.subway.path.domain.strategy.additional;

import wooteco.subway.path.domain.strategy.additional.age.UnderFive;

public class InfancyDiscount extends AgeDiscountPolicy {
    public InfancyDiscount(int discountFare) {
        super(new UnderFive(), discountFare);
    }

    @Override
    public int calculateFare(int fare) {
        return 0;
    }
}
