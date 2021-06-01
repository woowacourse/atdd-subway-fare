package wooteco.subway.member.domain;

import wooteco.subway.path.domain.Fare;

import java.util.Arrays;
import java.util.function.IntPredicate;
import java.util.function.UnaryOperator;

public enum AgeFarePolicy {
    YOUNG_AGE(AgeFarePolicy::youngPredicate, AgeFarePolicy::youngAgeDiscountPolicy),
    CHILDREN(AgeFarePolicy::childrenPredicate, AgeFarePolicy::childrenAgeDiscountPolicy),
    DEFAULT(age -> false, UnaryOperator.identity());

    private static final int BASE_DISCOUNT_AMOUNT = 350;

    private static final double YOUNG_AGE_DISCOUNT_RATIO = 0.8;
    private static final double CHILDREN_AGE_DISCOUNT_RATIO = 0.5;
    private static final int CHILDREN_MIN_AGE = 6;
    private static final int YOUNG_MIN_AGE = 13;
    private static final int YOUNG_MAX_AGE = 19;

    private IntPredicate agePredicate;
    private UnaryOperator<Fare> farePolicy;

    AgeFarePolicy(IntPredicate agePredicate, UnaryOperator<Fare> farePolicy) {
        this.agePredicate = agePredicate;
        this.farePolicy = farePolicy;
    }

    public static AgeFarePolicy of(int age) {
        return Arrays.stream(AgeFarePolicy.values())
                .filter(ageFarePolicy -> ageFarePolicy.agePredicate.test(age))
                .findAny()
                .orElse(DEFAULT);
    }

    private static boolean youngPredicate(int age) {
        return age >= YOUNG_MIN_AGE && age < YOUNG_MAX_AGE;
    }

    private static boolean childrenPredicate(int age) {
        return age >= CHILDREN_MIN_AGE && age < YOUNG_MIN_AGE;
    }

    private static Fare youngAgeDiscountPolicy(Fare fare) {
        return fare.minus(new Fare(BASE_DISCOUNT_AMOUNT)).multiply(YOUNG_AGE_DISCOUNT_RATIO);
    }

    private static Fare childrenAgeDiscountPolicy(Fare fare) {
        return fare.minus(new Fare(BASE_DISCOUNT_AMOUNT)).multiply(CHILDREN_AGE_DISCOUNT_RATIO);
    }

    public Fare calculateFare(Fare fare) {
        return this.farePolicy.apply(fare);
    }
}
