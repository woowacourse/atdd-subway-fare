package wooteco.subway.path.domain.strategy.additional;

import wooteco.subway.path.domain.strategy.additional.age.SixToTwelve;

public class ChildrenDiscount extends AgeDiscountPolicy {
    public ChildrenDiscount(int discountFare) {
        super(new SixToTwelve(), discountFare);
    }

    @Override
    public int calculateFare(int fare) {
        return (int) ((fare - discountFare()) * 0.5);
    }
}
