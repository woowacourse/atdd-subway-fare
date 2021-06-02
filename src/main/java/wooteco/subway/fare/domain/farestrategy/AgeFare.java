package wooteco.subway.fare.domain.farestrategy;

import wooteco.subway.fare.domain.Money;

import static wooteco.subway.fare.domain.Money.ZERO;

public class AgeFare implements FareStrategy {
    private static final int CHILD_START_AGE = 6;
    private static final int ADULT_START_AGE = 19;
    private static final int TEENAGER_START_AGE = 13;
    private static final int DISCOUNT_PRICE = 350;
    private static final double DISCOUNT_50_RATE = 0.5;
    private static final double DISCOUNT_20_RATE = 0.8;
    private final int age;

    public AgeFare(int age) {
        this.age = age;
    }

    @Override
    public Money calculate(Money value) {
        if (age < CHILD_START_AGE) {
            return ZERO;
        }
        if (age >= ADULT_START_AGE) {
            return value;
        }
        if (age < TEENAGER_START_AGE) {
            return value.subtract(DISCOUNT_PRICE)
                    .multiply(DISCOUNT_50_RATE);
        }
        return value.subtract(DISCOUNT_PRICE)
                .multiply(DISCOUNT_20_RATE);
    }
}
