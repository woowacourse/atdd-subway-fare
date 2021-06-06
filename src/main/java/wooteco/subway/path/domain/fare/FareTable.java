package wooteco.subway.path.domain.fare;

import wooteco.subway.member.domain.User;

import static wooteco.subway.path.domain.fare.DiscountPolicy.*;

public class FareTable {
    private final int adultFare;
    private final int teenFare;
    private final int childFare;
    private final int babyFare;
    private final int defaultFare;

    private FareTable(int adultFare, int teenFare, int childFare, int babyFare, int defaultFare) {
        this.adultFare = adultFare;
        this.teenFare = teenFare;
        this.childFare = childFare;
        this.babyFare = babyFare;
        this.defaultFare = defaultFare;
    }

    public static FareTable of(Fare fare, User member) {
        return new FareTable(
                ADULT.apply(fare),
                TEENAGER.apply(fare),
                CHILD.apply(fare),
                BABY.apply(fare),
                findByAge(fare, member));

    }

    private static int findByAge(Fare fare, User member) {
        if (member.isGuest()) {
            return ADULT.apply(fare);
        }
        DiscountPolicy memberDiscount = DiscountPolicy.findByAge(member.getAge());
        return memberDiscount.apply(fare);
    }

    public int getAdultFare() {
        return adultFare;
    }

    public int getTeenFare() {
        return teenFare;
    }

    public int getChildFare() {
        return childFare;
    }

    public int getBabyFare() {
        return babyFare;
    }

    public int getDefaultFare() {
        return defaultFare;
    }
}
