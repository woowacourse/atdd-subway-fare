package wooteco.subway.path.domain;

import java.util.Objects;

public class Money {
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

    public Money applyDiscount(DiscountPolicy discountPolicy) {
        return new Money((int) ((this.value - discountPolicy.getStaticDiscount()) * discountPolicy.getDiscountRate()));
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
