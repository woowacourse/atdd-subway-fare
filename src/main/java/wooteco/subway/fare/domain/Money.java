package wooteco.subway.fare.domain;

import java.math.BigDecimal;

public class Money {
    public static final Money DEFAULT_MONEY = new Money(1250);
    public static final Money ZERO = new Money(0);

    private final BigDecimal money;

    public Money(int money) {
        this(BigDecimal.valueOf(money));
    }

    public Money(BigDecimal money) {
        this.money = money;
    }

    public Money add(int value) {
        return new Money(this.money.add(BigDecimal.valueOf(value)));
    }

    public Money subtract(int value) {
        return new Money(this.money.subtract(BigDecimal.valueOf(value)));
    }

    public Money multiply(double value) {
        return new Money(this.money.multiply(BigDecimal.valueOf(value)));
    }

    public int toInt() {
        return money.intValue();
    }
}
