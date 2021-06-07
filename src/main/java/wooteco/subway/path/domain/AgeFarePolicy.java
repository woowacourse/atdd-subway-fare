package wooteco.subway.path.domain;

import java.util.Arrays;
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
        try {
            AgeFarePolicy farePolicy = findByAge(age);
            return farePolicy.calculateAgeFare(originFare);
        } catch (IllegalArgumentException e) {
            return originFare;
        }
    }

    private int calculateAgeFare(int fare) {
        return (int) ((fare - basicDiscountAmount) * ageDiscountRate);
    }

    public static AgeFarePolicy findByAge(int age) {
        return Arrays.stream(AgeFarePolicy.values())
            .filter(ageFarePolicy -> ageFarePolicy.policy.test(age))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("나이에 해당하는 요금 정책이 존재하지 않습니다."));
    }
}
