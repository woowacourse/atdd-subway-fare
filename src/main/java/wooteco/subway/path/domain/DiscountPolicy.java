package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DiscountPolicy {
    ADULT(Money.ZERO, 1.0, (age) -> age >= 19),
    TEENAGER(new Money(350), 0.8, (age) -> 13 <= age && age < 19),
    CHILD(new Money(350), 0.5, (age) -> 6 <= age && age < 13),
    BABY(Money.ZERO, 0.0, (age) -> 0 < age && age < 6);

    private final Money staticDiscount;
    private final double discountRate;
    private final Predicate<Integer> findAge;

    DiscountPolicy(Money staticDiscount, double discountRate, Predicate<Integer> findAge) {
        this.staticDiscount = staticDiscount;
        this.discountRate = discountRate;
        this.findAge = findAge;
    }

    public static DiscountPolicy findByAge(int age) {
        return Arrays.stream(DiscountPolicy.values())
                .filter(policy -> policy.findAge.test(age))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 나이 정보 입니다."));
    }

    public Money getStaticDiscount() {
        return staticDiscount;
    }

    public double getDiscountRate() {
        return discountRate;
    }
}