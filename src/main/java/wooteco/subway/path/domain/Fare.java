package wooteco.subway.path.domain;

import wooteco.subway.path.domain.strategy.DistanceAdditionPolicy;
import wooteco.subway.path.domain.strategy.additional.AgeDiscountPolicy;

public class Fare {
    private final DistanceAdditionPolicy distanceAdditionPolicy;
    private final AgeDiscountPolicy discountPolicy;
    private final int extraFare;

    public Fare(DistanceAdditionPolicy distanceAdditionPolicy, AgeDiscountPolicy discountPolicy, int extraFare) {
        this.distanceAdditionPolicy = distanceAdditionPolicy;
        this.discountPolicy = discountPolicy;
        this.extraFare = extraFare;
    }

    public int calculateFare() {
        int additionalFare = distanceAdditionPolicy.calculateFare(extraFare);
        return discountPolicy.calculateFare(additionalFare);
    }
}
