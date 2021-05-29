package wooteco.subway.path.domain;

import wooteco.subway.member.domain.LoginMember;

public class Price {
    private static final int DEFAULT_DISTANCE = 10;
    private static final int DEFAULT_PRICE = 1250;
    private static final int EXTRA_DISTANCE = 50;
    private static final int EXTRA_PRICE = 2050;
    private static final int UNIT_PRICE = 100;
    private static final int MAXIMUM_UNIT_DISTANCE = 8;
    private static final int MINIMUM_UNIT_DISTANCE = 5;
    private static final int DEDUCTION_PRICE = 350;
    private static final int DISCOUNT_FIRST_BOUNDARY_FROM_AGE = 6;
    private static final int DISCOUNT_SECOND_BOUNDARY_FROM_AGE = 13;
    private static final int DISCOUNT_THIRD_BOUNDARY_FROM_AGE = 19;
    private static final double DISCOUNT_RATE_50 = 0.5;
    private static final double DISCOUNT_RATE_20 = 0.2;

    private AgePolicy ageStrategy;
    private int price;

    public Price() {
    }

    public Price(int price) {
        this.price = price;
    }

    public void calculatePrice(int distance) {
        this.price = FareByDistance.calculate(distance);
    }


    public void addExtraPrice(int extraPrice) {
        this.price += extraPrice;
    }

    public void calculateDiscountRateFromAge(LoginMember loginMember) {
        this.price = FareByAge.calculate(price, loginMember.getAge());
    }

    public int getPrice() {
        return price;
    }
}
