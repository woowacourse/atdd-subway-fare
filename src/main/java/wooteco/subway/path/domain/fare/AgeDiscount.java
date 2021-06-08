package wooteco.subway.path.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;

public enum AgeDiscount {
    TEENAGER(age -> age >= 13 && age < 19, 0.2, 350),
    KID(age -> age >= 6 && age < 13, 0.5, 350),
    NOT_APPLICABLE(age -> false, 0, 350);

    private final Predicate<Integer> figureOutDiscount;
    private final double discountRate;
    private final int calculationFormulaValue;

    AgeDiscount(Predicate<Integer> figureOutDiscount, double discountRate, int calculationFormulaValue) {
        this.figureOutDiscount = figureOutDiscount;
        this.discountRate = discountRate;
        this.calculationFormulaValue = calculationFormulaValue;
    }

    public static int calculateFareAfterDiscount(int fare, int age) {
        AgeDiscount findAge = findDiscountPolicy(age);
        return fare - discountedFare(findAge, fare);
    }

    public static AgeDiscount findDiscountPolicy(int age) {
        return Arrays.stream(AgeDiscount.values())
                .filter(value -> value.figureOutDiscount.test(age))
                .findAny()
                .orElse(NOT_APPLICABLE);
    }

    private static int discountedFare(AgeDiscount findAge, int fare) {
        return (int) Math.ceil((fare - findAge.calculationFormulaValue) * findAge.discountRate);
    }
}
