package wooteco.subway.path.domain;

import static wooteco.subway.path.domain.DiscountPolicy.*;

public class FareTable {
    private final int adultFare;
    private final int teenagerFare;
    private final int childFare;
    private final int babyFare;

    private FareTable(int adultFare, int teenagerFare, int childFare, int babyFare) {
        this.adultFare = adultFare;
        this.teenagerFare = teenagerFare;
        this.childFare = childFare;
        this.babyFare = babyFare;
    }

    public static FareTable of(FarePolicy fare) {
        return new FareTable(fare.apply(ADULT), fare.apply(TEENAGER), fare.apply(CHILD), fare.apply(CHILD));
    }

    public int findByAge(FarePolicy fare, int age) {
        DiscountPolicy memberDiscount = DiscountPolicy.findByAge(age);
        return fare.apply(memberDiscount);
    }

    public int getAdultFare() {
        return adultFare;
    }

    public int getTeenager() {
        return teenagerFare;
    }

    public int getChild() {
        return childFare;
    }

    public int getBaby() {
        return babyFare;
    }
}
