package wooteco.subway.path.domain;

import java.util.Objects;

public class Money {
    private static final double FIRST_BOUND_REFERENCE_PER_DISTANCE = 5.0;
    private static final double SECOND_BOUND_REFERENCE_PER_DISTANCE = 8.0;
    private static final int FIRST_BOUND = 10;
    private static final int SECOND_BOUND = 50;
    private static final Money ZERO = new Money(0);
    private static final Money FARE_PER_DISTANCE = new Money(100);
    private static final Money DEFAULT_FARE = new Money(1250);

    private final int value;

    public Money(int value) {
        validateMoney(value);
        this.value = value;
    }

    private void validateMoney(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("돈은 0보다 커야합니다.");
        }
    }

    public static Money calculateFareByDistance(int distance) {
        if (distance > SECOND_BOUND) {
            return DEFAULT_FARE.plusFirstBound(SECOND_BOUND).plusSecondBound(distance);
        }
        return DEFAULT_FARE.plusFirstBound(distance);
    }

    public Money plusFirstBound(int distance) {
        if (distance < FIRST_BOUND) {
            return this.add(ZERO);
        }
        return this.add(calculateFarePerDistance(distance, FIRST_BOUND, FIRST_BOUND_REFERENCE_PER_DISTANCE, FARE_PER_DISTANCE));
    }

    public Money plusSecondBound(int distance) {
        return this.add(calculateFarePerDistance(distance, SECOND_BOUND, SECOND_BOUND_REFERENCE_PER_DISTANCE, FARE_PER_DISTANCE));
    }

    public Money calculateFarePerDistance(int distance, int startingPoint , double perDistance, Money additionalFare) {
        return new Money((int) (Math.ceil((distance - startingPoint) / perDistance) * additionalFare.value));
    }

    public Money applyDiscount(DiscountPolicy discountPolicy) {
        return new Money((int) ((this.value - discountPolicy.getStaticDiscount()) * discountPolicy.getDiscountRate()));
    }

    public Money add(Money money) {
        return new Money(this.value + money.value);
    }

    public static Money zero() {
        return ZERO;
    }

    public int value() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return value == money.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
