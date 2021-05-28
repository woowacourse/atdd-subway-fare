package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DiscountPolicy {
    ADULT("어른", 0, 1.0, (age) -> age >= 19),
    TEENAGER("청소년",350, 0.8, (age) -> age >= 13 && age < 19),
    CHILD("어린이",350, 0.5, (age) -> age >= 6 && age < 13),
    INFANT("유아", 0, 0, (age) -> age < 6);

    private final String korean;
    private final int defaultDeduction;
    private final double discountRate;
    private final Predicate<Integer> agePolicy;

    DiscountPolicy(String korean, int defaultDeduction, double discountRate,
        Predicate<Integer> agePolicy) {
        this.korean = korean;
        this.defaultDeduction = defaultDeduction;
        this.discountRate = discountRate;
        this.agePolicy = agePolicy;
    }

    public int getDefaultDeduction() {
        return defaultDeduction;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public String getKorean() {
        return korean;
    }

    public static DiscountPolicy findAge(int age) {
        return Arrays.stream(values())
            .filter(value -> value.agePolicy.test(age))
            .findFirst()
            .orElse(ADULT);
    }
}
