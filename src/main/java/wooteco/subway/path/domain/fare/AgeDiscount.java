package wooteco.subway.path.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum AgeDiscount {
    TEENAGER(age -> age >= 13 && age < 19, teenagerDistanceDiscountPolicy()),
    KID(age -> age >= 6 && age < 13, kidDiscountPolicy()),
    NOT_APPLICABLE(age -> false, fare -> fare);

    AgeDiscount(Predicate<Integer> figureOutDiscount, UnaryOperator<Integer> discountPolicy) {
        this.figureOutDiscount = figureOutDiscount;
        this.discountPolicy = discountPolicy;
    }

    Predicate<Integer> figureOutDiscount;
    UnaryOperator<Integer> discountPolicy;

    public static int calculateFareAfterDiscount(int distance) {
        AgeDiscount findAge = Arrays.stream(AgeDiscount.values())
                .filter(value -> value.figureOutDiscount.test(distance))
                .findAny()
                .orElse(NOT_APPLICABLE);

        return findAge.discountPolicy.apply(distance);
    }

    static UnaryOperator<Integer> kidDiscountPolicy() {
        return fare -> fare - (int) Math.ceil((fare - 350) * 0.2);
    }

    static UnaryOperator<Integer> teenagerDistanceDiscountPolicy() {
        return fare -> fare - (int) Math.ceil((fare - 350) * 0.5);
    }
}
