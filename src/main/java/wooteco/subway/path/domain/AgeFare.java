package wooteco.subway.path.domain;

import wooteco.subway.exception.SubwayCustomException;
import wooteco.subway.path.exception.PathException;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.IntPredicate;

public enum AgeFare {

    ADULT(fare -> 0,
            age -> age >= AgeFare.ADULT_AGE),
    TEEN(AgeFare::calculateFareWithTeen,
            age -> age >= AgeFare.TEEN_AGE && age < AgeFare.ADULT_AGE),
    CHILD(AgeFare::calculateFareWithCHILD,
            age -> age >= AgeFare.CHILD_AGE && age < AgeFare.TEEN_AGE),
    BABY(fare -> fare,
            age -> age < AgeFare.CHILD_AGE);

    private static final int ADULT_AGE = 19;
    private static final int TEEN_AGE = 13;
    private static final int CHILD_AGE = 6;
    private static final int DISCOUNT_MONEY = 350;
    private static final double TEEN_DISCOUNT_RATE = 0.2;
    private static final double CHILD_DISCOUNT_RATE = 0.5;

    private final Function<Integer, Integer> fareFunction;
    private final IntPredicate agePredicate;

    AgeFare(Function<Integer, Integer> fareFunction, IntPredicate agePredicate) {
        this.fareFunction = fareFunction;
        this.agePredicate = agePredicate;
    }

    public static AgeFare of(int age) {
        return Arrays.stream(AgeFare.values())
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
        return (int) ((fare - AgeFare.DISCOUNT_MONEY) * AgeFare.CHILD_DISCOUNT_RATE);
    }

}
