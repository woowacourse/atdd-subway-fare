package wooteco.subway.path.domain.strategy.additional;

import wooteco.subway.path.domain.strategy.additional.age.AgeRange;

public abstract class AgeDiscountPolicy {
    private final AgeRange ageRange;
    private final int discountFare;

    protected AgeDiscountPolicy(AgeRange ageRange, int discountFare) {
        this.ageRange = ageRange;
        this.discountFare = discountFare;
    }

    protected final int discountFare() {
        return discountFare;
    }

    public final boolean match(int age) {
        return ageRange.match(age);
    }

    public abstract int calculateFare(int fare);
}
