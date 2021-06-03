package wooteco.subway.path.domain.strategy.additional;

import wooteco.subway.path.domain.strategy.additional.age.ThirteenToNineTeen;

public class AdolescenceDiscount extends AgeDiscountPolicy {
    public AdolescenceDiscount(int discountFare) {
        super(new ThirteenToNineTeen(), discountFare);
    }

    @Override
    public int calculateFare(int fare) {
        return (int) ((fare - discountFare()) * 0.8);
    }
}
