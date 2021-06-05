package wooteco.subway.member.domain;

import java.util.Arrays;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

public enum AgeDiscountPolicy {
    BABY(age -> age > 0 && age < 6,
        fare -> 0),
    CHILDREN(age -> age >= 6 && age < 13,
        fare -> (int) ((fare - 350) * 0.5)),
    TEENAGER(age -> age >= 13 && age < 19,
        fare -> (int) ((fare - 350) * 0.8)),
    ADULT(age -> age >= 19,
        fare -> fare);

    private final IntPredicate checkAge;
    private final IntFunction<Integer> calculatedFare;

    AgeDiscountPolicy(IntPredicate checkAge, IntFunction<Integer> calculatedFare) {
        this.checkAge = checkAge;
        this.calculatedFare = calculatedFare;
    }

    public static AgeDiscountPolicy of(int age) {
        return Arrays.stream(AgeDiscountPolicy.values())
            .filter(it -> it.checkAge.test(age))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException("해당 나이에 해당하는 요금 정책이 없습니다."));
    }

    public int calculateFare(int fare) {
        return calculatedFare.apply(fare);
    }
}
