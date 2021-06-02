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
        if (isOverSecondBound(distance)) {
            return DEFAULT_FARE.plusFirstBound(SECOND_BOUND).plusSecondBound(distance);
        }
        return DEFAULT_FARE.plusFirstBound(distance);
    }

    private static boolean isOverSecondBound(int distance) {
        return distance > SECOND_BOUND;
    }

    private Money plusFirstBound(int distance) {
        if (isDefaultFareDistance(distance)) {
            return this.plus(ZERO);
        }
        return this.plus(calculateFarePerDistance(distance, FIRST_BOUND, FIRST_BOUND_REFERENCE_PER_DISTANCE, FARE_PER_DISTANCE));
    }

    private static boolean isDefaultFareDistance(int distance) {
        return distance < FIRST_BOUND;
    }

    private Money plusSecondBound(int distance) {
        return this.plus(calculateFarePerDistance(distance, SECOND_BOUND, SECOND_BOUND_REFERENCE_PER_DISTANCE, FARE_PER_DISTANCE));
    }

    private Money calculateFarePerDistance(int distance, int startingPoint, double perDistance, Money additionalFare) {
        return new Money((int) (Math.ceil((distance - startingPoint) / perDistance) * additionalFare.value));
    }

    public Money applyDiscount(DiscountPolicy discountPolicy) {
        return this.minus(discountPolicy.getStaticDiscount()).multiple(discountPolicy.getDiscountRate());
    }

    public boolean afterDiscountIsNegative(Money staticDiscount) {
        return (this.value - staticDiscount.value) < 0;
    }

    public Money multiple(double discountRate) {
        return new Money((int) (this.value * discountRate));
    }

    public Money minus(Money money) {
        return new Money(this.value - money.value);
    }

    public Money plus(Money money) {
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
