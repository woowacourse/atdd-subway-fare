package wooteco.subway.path.domain;

public class Fare {
    private static final int DEFAULT_FARE = 1250;
    private static final int FIRST_BOUND = 10;
    private static final int SECOND_BOUND = 50;
    private static final int ZERO = 0;

    private final Money money;

    private Fare(Money money) {
        this.money = money;
    }

    public static Fare calculateFare(int distance) {
        return new Fare(new Money(calculateFareByDistance(distance)));
    }

    private static int calculateFareByDistance(int distance) {
        if (distance > SECOND_BOUND) {
            return DEFAULT_FARE + plusFirstBound(SECOND_BOUND) + plusSecondBound(distance);
        }
        return DEFAULT_FARE + plusFirstBound(distance);
    }

    private static int plusFirstBound(int distance) {
        if (distance < FIRST_BOUND) {
            return ZERO;
        }
        return (int) (Math.ceil((distance - FIRST_BOUND) / 5.0) * 100);
    }

    private static int plusSecondBound(int distance) {
        return (int) (Math.ceil((distance - SECOND_BOUND) / 8.0) * 100);
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
