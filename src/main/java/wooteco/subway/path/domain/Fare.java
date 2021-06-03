package wooteco.subway.path.domain;

import java.util.Objects;
import wooteco.subway.path.domain.discount.AgeDiscountRule;

public class Fare {

    public static final Fare ZERO = new Fare(0);
    public static final Fare BASIC = new Fare(1_250);
    public static final Fare DEDUCTION = new Fare(350);
    public static final Fare ADDITION = new Fare(100);

    private final int value;

    public Fare(int value) {
        validateNotNegative(value);
        this.value = value;
    }

    private void validateNotNegative(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }
    }

    public static Fare ofSubwayFare(int distance, Fare maxExtraFare, int age) {
        Fare fareOfDistance = DistanceFareRule.calculateRate(distance);
        Fare totalFare = fareOfDistance.add(maxExtraFare);
        return AgeDiscountRule.discountByAge(age, totalFare);
    }

    public static Fare calculateRate(int distance, Fare maxExtraFare) {
        validatePositiveDistance(distance);
        Fare fareOfDistance = DistanceFareRule.calculateRate(distance);
        return fareOfDistance.add(maxExtraFare);
    }

    private static void validatePositiveDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 0보다 커야합니다.");
        }
    }

    public Fare add(Fare other) {
        return new Fare(this.value + other.value);
    }

    public Fare subtract(Fare other) {
        return new Fare(this.value - other.value);
    }

    public Fare multiply(double multiplier) {
        return new Fare((int) (this.value * multiplier));
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare = (Fare) o;
        return Objects.equals(value, fare.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
