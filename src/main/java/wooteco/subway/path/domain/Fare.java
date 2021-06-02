package wooteco.subway.path.domain;

import java.util.Objects;

public class Fare {
    private final Money money;

    private Fare(Money money) {
        this.money = money;
    }

    public static Fare of(Money money) {
        return new Fare(money);
    }

    public static Fare calculateFare(int distance, Fare extraFare) {
        return new Fare(Money.calculateFareByDistance(distance).plus(extraFare.money));
    }

    public int money() {
        return money.value();
    }

    public Fare discount(DiscountPolicy discountPolicy) {
        if (this.money.afterDiscountIsNegative(discountPolicy.getStaticDiscount())) {
            return new Fare(Money.zero());
        }
        return new Fare(money.applyDiscount(discountPolicy));
    }

    public Fare discountByAge(int age) {
        return discount(DiscountPolicy.findByAge(age));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare = (Fare) o;
        return Objects.equals(money, fare.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }
}
