package wooteco.subway.path.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {

    private static final Price ZERO = new Price(0);
    public static final Price BASIC = new Price(1_250);
    public static final Price ADDITION = new Price(100);

    private final BigDecimal value;

    public Price(int value) {
        this(BigDecimal.valueOf(value));
    }

    public Price(BigDecimal value) {
        validateNotNegative(value);
        this.value = value;
    }

    private void validateNotNegative(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) == -1) {
            throw new IllegalArgumentException("가격은 음수가 될 수 없습니다.");
        }
    }

    public static Price calculateRate(int distance) {
        validatePositiveDistance(distance);
        Price currentPrice = Price.ZERO;
        int currentDistance = distance;
        if (currentDistance > 50) {
            int longDistance = currentDistance - 50;
            int additionalPrice = calculateLongDistancePrice(longDistance);
            currentPrice = currentPrice.add(new Price(additionalPrice));
            currentDistance = 50;
        }

        if (currentDistance > 10) {
            int middleDistance = currentDistance - 10;
            int additionalPrice = calculateMiddleDistancePrice(middleDistance);
            currentPrice = currentPrice.add(new Price(additionalPrice));
        }

        return currentPrice.add(Price.BASIC);
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

    public Price add(Price other) {
        return new Price(this.value.add(other.value));
    }

    public BigDecimal getValue() {
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
        Price price = (Price) o;
        return Objects.equals(value, price.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
