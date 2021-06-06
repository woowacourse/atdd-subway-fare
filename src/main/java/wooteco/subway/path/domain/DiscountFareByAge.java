package wooteco.subway.path.domain;

import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.path.exception.PathException;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntPredicate;

public enum DiscountFareByAge {

    ADULT(fare -> 0,
            age -> age >= DiscountFareByAge.ADULT_AGE),
    TEEN(DiscountFareByAge::calculateFareWithTeen,
            age -> age >= DiscountFareByAge.TEEN_AGE && age < DiscountFareByAge.ADULT_AGE),
    CHILD(DiscountFareByAge::calculateFareWithCHILD,
            age -> age >= DiscountFareByAge.CHILD_AGE && age < DiscountFareByAge.TEEN_AGE),
    BABY(fare -> fare,
            age -> age < DiscountFareByAge.CHILD_AGE);

    private static final int ADULT_AGE = 19;
    private static final int TEEN_AGE = 13;
    private static final int CHILD_AGE = 6;
    private static final int DISCOUNT_MONEY = 350;
    private static final double TEEN_DISCOUNT_RATE = 0.2;
    private static final double CHILD_DISCOUNT_RATE = 0.5;

    private final Function<Integer, Integer> fareFunction;
    private final IntPredicate agePredicate;

    DiscountFareByAge(Function<Integer, Integer> fareFunction, IntPredicate agePredicate) {
        this.fareFunction = fareFunction;
        this.agePredicate = agePredicate;
    }

    public static DiscountFareByAge of(int age) {
        return Arrays.stream(DiscountFareByAge.values())
                .filter(item -> item.agePredicate.test(age))
                .findAny()
                .orElseThrow(() -> new SubwayCustomException(PathException.INVALID_AGE_EXCEPTION));
    }

    public int calculateByAge(int fare) {
        return this.fareFunction.apply(fare);
    }

    private static Integer calculateFareWithTeen(int fare) {
        return (int) ((fare - DISCOUNT_MONEY) * TEEN_DISCOUNT_RATE);
    }

    private static Integer calculateFareWithCHILD(int fare) {
        return (int) ((fare - DiscountFareByAge.DISCOUNT_MONEY) * DiscountFareByAge.CHILD_DISCOUNT_RATE);
    }

}
