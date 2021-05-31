package wooteco.subway.fare.domain;

import wooteco.subway.exception.badrequest.fare.InvalidFareArgumentException;

import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public enum AgeDiscountStrategy implements DiscountStrategy {
    INFANT(age -> age < 6, fare -> 0),
    CHILD(age -> 6 <= age && age < 13, fare -> (fare - AgeDiscountStrategy.DISCOUNT_FARE) / AgeDiscountStrategy.CHILD_DISCOUNT_RATE),
    ADOLESCENT(age -> 13 <= age && age < 19, fare -> (int) ((fare - AgeDiscountStrategy.DISCOUNT_FARE) * AgeDiscountStrategy.ADOLESCENT_DISCOUNT_RATE)),
    ADULT(age -> age >= 19, UnaryOperator.identity());

    private static final int DISCOUNT_FARE = 350;
    private static final int CHILD_DISCOUNT_RATE = 2;
    private static final double ADOLESCENT_DISCOUNT_RATE = 0.8;

    private final Predicate<Integer> policy;
    private final UnaryOperator<Integer> discountAlgorithm;

    AgeDiscountStrategy(Predicate<Integer> policy, UnaryOperator<Integer> discountAlgorithm) {
        this.policy = policy;
        this.discountAlgorithm = discountAlgorithm;
    }

    public static AgeDiscountStrategy find(int age) {
        return Arrays.stream(AgeDiscountStrategy.values())
                .filter(ageDiscountStrategy -> ageDiscountStrategy.policy.test(age))
                .findFirst()
                .orElseThrow(InvalidFareArgumentException::new);
    }

    @Override
    public int applyDiscount(int fare) {
        return discountAlgorithm.apply(fare);
    }
}
