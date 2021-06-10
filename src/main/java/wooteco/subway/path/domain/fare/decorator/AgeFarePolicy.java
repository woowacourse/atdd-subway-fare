package wooteco.subway.path.domain.fare.decorator;

import wooteco.subway.path.application.PathException;

import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

enum AgeFarePolicy {
    BABY((age) -> age < 6, AgeFarePolicy::baby),
    CHILD((age) -> age >= 6 && age < 13, AgeFarePolicy::child),
    TEENAGER((age) -> age >= 13 && age < 19, AgeFarePolicy::teenager),
    ADULT((age) -> age >= 19, AgeFarePolicy::adult);

    private static final int BABY_FARE = 0;
    private static final int DISCOUNT_DIFF = 350;
    private static final double CHILD_AFTER_DISCOUNT_RATIO = 0.5;
    private static final double TEENAGER_AFTER_DISCOUNT_RATIO = 0.8;

    private IntPredicate checkAgeRange;
    private IntUnaryOperator calculate;

    AgeFarePolicy(IntPredicate checkAgeRange, IntUnaryOperator calculate) {
        this.checkAgeRange = checkAgeRange;
        this.calculate = calculate;
    }

    public static AgeFarePolicy of(int age) {
        return Arrays.stream(values())
                .filter(ageFarePolicy -> ageFarePolicy.checkAgeRange.test(age))
                .findFirst()
                .orElseThrow(() -> new PathException("존재하지 않는 나이 멤버입니다."));
    }

    public static int baby(int fare) {
        return BABY_FARE;
    }

    public static int child(int fare) {
        return (int) ((fare - DISCOUNT_DIFF) * CHILD_AFTER_DISCOUNT_RATIO);
    }

    public static int teenager(int fare) {
        return (int) ((fare - DISCOUNT_DIFF) * TEENAGER_AFTER_DISCOUNT_RATIO);
    }

    public static int adult(int fare) {
        return fare;
    }

    public int calculate(int fare) {
        return calculate.applyAsInt(fare);
    }
}
