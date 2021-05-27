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

    private int price;

    public Price() {
    }

    public Price(int price) {
        this.price = price;
    }

    public void calculatePrice(int distance) {
        if (distance <= DEFAULT_DISTANCE) {
            this.price = DEFAULT_PRICE;
            return;
        }
        if (distance <= EXTRA_DISTANCE) {
            this.price = (int)(DEFAULT_PRICE + calculateExtraPrice(distance, DEFAULT_DISTANCE, MINIMUM_UNIT_DISTANCE));
            return;
        }
        if (distance > EXTRA_DISTANCE) {
            this.price = (int)(EXTRA_PRICE + calculateExtraPrice(distance, EXTRA_DISTANCE, MAXIMUM_UNIT_DISTANCE));
        }
    }

    private double calculateExtraPrice(int distance, int extraDistance, int unitDistance) {
        return Math.ceil((distance - extraDistance - 1) / unitDistance + 1) * UNIT_PRICE;
    }

    public void addExtraPrice(int extraPrice) {
        this.price += extraPrice;
    }

    public void calculateDiscountRateFromAge(LoginMember loginMember) {
        if (loginMember.getAge() == null) {
            return;
        }
        if (loginMember.getAge() < DISCOUNT_FIRST_BOUNDARY_FROM_AGE) {
            this.price = 0;
            return;
        }
        if (loginMember.getAge() < DISCOUNT_SECOND_BOUNDARY_FROM_AGE) {
            this.price -= DEDUCTION_PRICE;
            this.price -= (int)(this.price * DISCOUNT_RATE_50);
            return;
        }
        if (loginMember.getAge() < DISCOUNT_THIRD_BOUNDARY_FROM_AGE) {
            this.price -= DEDUCTION_PRICE;
            this.price -= (int)(this.price * DISCOUNT_RATE_20);
        }
    }

    public int getPrice() {
        return price;
    }
}
