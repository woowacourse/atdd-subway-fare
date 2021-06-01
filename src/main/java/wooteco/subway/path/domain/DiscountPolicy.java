package wooteco.subway.path.domain;

import wooteco.subway.path.application.InvalidPathException;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DiscountPolicy {
    BABY(age -> age < 6, 0, 1),
    CHILD(age -> age >= 6 && age < 13, 350, 0.5),
    YOUTH(age -> age >= 13 && age < 19, 350, 0.2),
    ADULT(age -> age >= 19, 0, 0);

    Predicate<Integer> agePredicate;
    int discountFare;
    double discountRate;

    DiscountPolicy(Predicate<Integer> agePredicate, int discountFare, double discountRate) {
        this.agePredicate = agePredicate;
        this.discountFare = discountFare;
        this.discountRate = discountRate;
    }

    public static int apply(int age, int fare) {
        return Arrays.stream(values())
                     .filter(it -> it.agePredicate.test(age))
                     .map(it -> (int) Math.ceil((fare - it.discountFare) * it.discountRate + it.discountFare))
                     .findAny()
                     .orElseThrow(() -> new InvalidPathException("나이별 할인요금을 구할 수 없습니다."));
    }
}
