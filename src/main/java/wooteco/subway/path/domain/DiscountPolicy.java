package wooteco.subway.path.domain;

import wooteco.subway.member.domain.Age;
import wooteco.subway.path.application.InvalidPathException;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DiscountPolicy {

    DEFAULT(Age::isAnonymousUserAge, 0, 0),
    BABY(age -> age.getAge() >= 0 && age.getAge() < 6, 0, 1),
    CHILD(age -> age.getAge() >= 6 && age.getAge() < 13, 350, 0.5),
    YOUTH(age -> age.getAge() >= 13 && age.getAge() < 19, 350, 0.2),
    ADULT(age -> age.getAge() >= 19, 0, 0);

    private final Predicate<Age> agePredicate;
    private final int discountFare;
    private final double discountRate;

    DiscountPolicy(Predicate<Age> agePredicate, int discountFare, double discountRate) {
        this.agePredicate = agePredicate;
        this.discountFare = discountFare;
        this.discountRate = discountRate;
    }

    public static int apply(Age age, int fare) {
        return Arrays.stream(values())
                     .filter(it -> it.agePredicate.test(age))
                     .map(it -> (int) Math.ceil((fare - it.discountFare) * it.discountRate + it.discountFare))
                     .findAny()
                     .orElseThrow(() -> new InvalidPathException("나이별 할인요금을 구할 수 없습니다."));
    }
}
