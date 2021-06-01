package wooteco.subway.path.domain.fare;

import wooteco.subway.member.domain.User;

import static wooteco.subway.path.domain.fare.DiscountPolicy.*;

public class FareTable {
    private final int adultFare;
    private final int teenagerFare;
    private final int childFare;
    private final int babyFare;
    private final int defaultFare;

    private FareTable(int adultFare, int teenagerFare, int childFare, int babyFare, int defaultFare) {
        this.adultFare = adultFare;
        this.teenagerFare = teenagerFare;
        this.childFare = childFare;
        this.babyFare = babyFare;
        this.defaultFare = defaultFare;
    }

    public static FareTable of(Fare fare, User member) {
        return new FareTable(
                fare.apply(ADULT),
                fare.apply(TEENAGER),
                fare.apply(CHILD),
                fare.apply(BABY),
                findByAge(fare, member)
        );
    }

    private static int findByAge(Fare fare, User member) {
        if (member.isGuest()) {
            return fare.apply(ADULT);
        }
        DiscountPolicy memberDiscount = DiscountPolicy.findByAge(member.getAge());
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

    public int getDefaultFare() {
        return defaultFare;
    }
}
