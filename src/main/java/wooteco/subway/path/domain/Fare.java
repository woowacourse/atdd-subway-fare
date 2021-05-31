package wooteco.subway.path.domain;

import java.util.Objects;

public class Fare {

    private static final Fare ZERO = new Fare(0);
    public static final Fare BASIC = new Fare(1_250);
    private static final Fare DEDUCTION = new Fare(350);
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

    public static Fare calculateRate(int distance, int maxExtraFare) {
        Fare fareOfDistanceRule = calculateRateByDistance(distance);
        return fareOfDistanceRule.add(new Fare(maxExtraFare));
    }

    public static Fare calculateRateByDistance(int distance) {
        validatePositiveDistance(distance);
        Fare currentFare = Fare.ZERO;
        int currentDistance = distance;
        if (currentDistance > 50) {
            int longDistance = currentDistance - 50;
            int additionalPrice = calculateLongDistancePrice(longDistance);
            currentFare = currentFare.add(new Fare(additionalPrice));
            currentDistance = 50;
        }

        if (currentDistance > 10) {
            int middleDistance = currentDistance - 10;
            int additionalPrice = calculateMiddleDistancePrice(middleDistance);
            currentFare = currentFare.add(new Fare(additionalPrice));
        }

        return currentFare.add(Fare.BASIC);
    }


    private static void validatePositiveDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException("거리는 0보다 커야합니다.");
        }
    }

    private static int calculateMiddleDistancePrice(int middleDistance) {
        return (int) ((Math.ceil((middleDistance - 1) / 5) + 1) * 100);
    }

    private static int calculateLongDistancePrice(int longDistance) {
        return (int) ((Math.ceil((longDistance - 1) / 8) + 1) * 100);
    }

    public Fare add(Fare other) {
        return new Fare(this.value + other.value);
    }

    public Fare subtract(Fare other) {
        return new Fare(this.value - other.value);
    }

    private int multiply(double multiplier) {
        return (int) (this.value * multiplier);
    }

    public int getValue() {
        return value;
    }

    public Fare discountByAge(int age) {
        if (age >= 20) {
            return ZERO;
        }

        Fare basicFare = this.subtract(DEDUCTION);
        if (age >= 13) {
            return new Fare(basicFare.multiply(0.2));
        }

        if (age >= 6) {
            return new Fare(basicFare.multiply(0.5));
        }

        return this;
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
