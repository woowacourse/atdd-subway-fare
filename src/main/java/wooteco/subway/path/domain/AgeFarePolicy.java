package wooteco.subway.path.domain;

import java.util.function.Predicate;

public enum AgeFarePolicy {

    THIRTEEN_OVER_NINETEEN_LESS_FARE_POLICY(age -> age >= 13 && age < 19, 0.8, 350),
    SIX_OVER_THIRTEEN_LESS_FARE_POLICY(age -> age >= 6 && age < 13, 0.5, 350);

    private final Predicate<Integer> policy;
    private final double ageDiscountRate;
    private final int basicDiscountAmount;

    AgeFarePolicy(Predicate<Integer> policy, double ageDiscountRate, int basicDiscountAmount) {
        this.policy = policy;
        this.ageDiscountRate = ageDiscountRate;
        this.basicDiscountAmount = basicDiscountAmount;
    }

    public static int agePolicyAppliedFare(int originFare, int age) {
        for (AgeFarePolicy ageFarePolicy : AgeFarePolicy.values()) {
            if (ageFarePolicy.policy.test(age)) {
                return ageFarePolicy.calculateAgeFare(originFare);
            }
        }
        return originFare;
    }

    private int calculateAgeFare(int fare) {
        return (int) ((fare - basicDiscountAmount) * ageDiscountRate);
    }

}
