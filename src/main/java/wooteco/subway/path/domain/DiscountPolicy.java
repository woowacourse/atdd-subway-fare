package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DiscountPolicy {
    ADULT(0, 1.0, (age) -> age >= 19),
    TEENAGER(350, 0.8, (age) -> 13 <= age && age <= 18),
    CHILD(350, 0.5, (age) -> 6 <= age && age <= 12),
    BABY(0, 0.0, (age) -> age < 6);

    private final int staticDiscount;
    private final double discountRate;
    private final Predicate<Integer> findAge;

    DiscountPolicy(int staticDiscount, double discountRate, Predicate<Integer> findAge) {
        this.staticDiscount = staticDiscount;
        this.discountRate = discountRate;
        this.findAge = findAge;
    }

    public int getStaticDiscount() {
        return staticDiscount;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public static DiscountPolicy findByAge(int age) {
        return Arrays.stream(DiscountPolicy.values())
                .filter(policy -> policy.findAge.test(age))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 나이 정보 입니다."));
    }
}