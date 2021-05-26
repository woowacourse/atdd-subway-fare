package wooteco.subway.path.domain.fare;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum AgeDiscount {
    TEENAGER(age -> age >= 13 && age < 19, fare -> fare - (int) Math.ceil((fare - 350) * 0.2)),
    KID(age -> age >= 6 && age < 13, fare -> fare - (int) Math.ceil((fare - 350) * 0.5)),
    NOT_APPLICABLE(age -> false, fare -> fare);

    AgeDiscount(Predicate<Integer> figureOutDiscount, UnaryOperator<Integer> discountPolicy) {
        this.figureOutDiscount = figureOutDiscount;
        this.discountPolicy = discountPolicy;
    }

    Predicate<Integer> figureOutDiscount;
    UnaryOperator<Integer> discountPolicy;

    public static int calculateFareAfterDiscount(int fare, int age) {
        AgeDiscount findAge = Arrays.stream(AgeDiscount.values())
                .filter(value -> value.figureOutDiscount.test(age))
                .findAny()
                .orElse(NOT_APPLICABLE);

        return findAge.discountPolicy.apply(fare);
    }
}
