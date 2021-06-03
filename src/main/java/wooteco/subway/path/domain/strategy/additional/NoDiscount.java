package wooteco.subway.path.domain.strategy.additional;

import wooteco.subway.path.domain.strategy.additional.age.OverTwenty;

public class NoDiscount extends AgeDiscountPolicy {
    public NoDiscount(int discountFare) {
        super(new OverTwenty(), discountFare);
    }

    @Override
    public int calculateFare(int fare) {
        return fare;
    }
}
