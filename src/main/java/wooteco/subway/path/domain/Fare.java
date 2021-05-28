package wooteco.subway.path.domain;

public class Fare {
    private final Money money;

    private Fare(Money money) {
        this.money = money;
    }

    public static Fare of(Money money) {
        return new Fare(money);
    }

    public static Fare calculateFare(int distance, Fare extraFare) {
        return new Fare(Money.calculateFareByDistance(distance).add(extraFare.money));
    }

    public int money() {
        return money.value();
    }

    public Fare discount(DiscountPolicy discountPolicy) {
        return new Fare(money.applyDiscount(discountPolicy));
    }

    public Fare discountByAge(int age) {
        return discount(DiscountPolicy.findByAge(age));
    }
}
