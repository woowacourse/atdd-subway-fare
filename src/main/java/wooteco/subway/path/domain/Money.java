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
            return DEFAULT_FARE.addFirstBound(SECOND_BOUND).addSecondBound(distance);
        }
        return DEFAULT_FARE.addFirstBound(distance);
    }

    private static boolean isOverSecondBound(int distance) {
        return distance > SECOND_BOUND;
    }

    private Money addFirstBound(int distance) {
        if (isDefaultFareDistance(distance)) {
            return this.add(ZERO);
        }
        return this.add(calculateFarePerDistance(distance, FIRST_BOUND, FIRST_BOUND_REFERENCE_PER_DISTANCE, FARE_PER_DISTANCE));
    }

    private static boolean isDefaultFareDistance(int distance) {
        return distance < FIRST_BOUND;
    }

    private Money addSecondBound(int distance) {
        return this.add(calculateFarePerDistance(distance, SECOND_BOUND, SECOND_BOUND_REFERENCE_PER_DISTANCE, FARE_PER_DISTANCE));
    }

    private Money calculateFarePerDistance(int distance, int startingPoint, double perDistance, Money additionalFare) {
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
