package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DiscountPolicy {
    ADULT("어른", 0, 1.0, (age) -> age >= 19),
    TEENAGER("청소년", 350, 0.8, (age) -> age >= 13 && age < 19),
    CHILD("어린이", 350, 0.5, (age) -> age >= 6 && age < 13),
    INFANT("유아", 0, 0, (age) -> age < 6);

    private final String korean;
    private final int deductionFare;
    private final double discountRate;
    private final Predicate<Integer> agePolicy;

    DiscountPolicy(String korean, int deductionFare, double discountRate, Predicate<Integer> agePolicy) {
        this.korean = korean;
        this.deductionFare = deductionFare;
        this.discountRate = discountRate;
        this.agePolicy = agePolicy;
    }

    public static DiscountPolicy findAge(int age) {
        return Arrays.stream(values())
                .filter(value -> value.agePolicy.test(age))
                .findAny()
                .orElse(ADULT);
    }

    public String getKorean() {
        return korean;
    }

    public int getDeductionFare() {
        return deductionFare;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public Predicate<Integer> getAgePolicy() {
        return agePolicy;
    }
}
