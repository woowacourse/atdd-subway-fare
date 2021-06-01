package wooteco.subway.path.domain.fare.age;

import wooteco.subway.member.domain.LoginMember;

import java.util.Arrays;
import java.util.function.Predicate;

public enum AgeDiscountType {
    BABY(new BabyDiscountStrategy(), age -> age < 6),
    CHILD(new ChildDiscountStrategy(), age -> age >= 6 && age < 13),
    TEENAGER(new TeenagerDiscountStrategy(), age -> age >= 13 && age < 19),
    DEFAULT(new DefaultDiscountStrategy(), age -> age >= 19);

    private final AgeStrategy strategy;
    private final Predicate<Integer> match;

    AgeDiscountType(AgeStrategy strategy, Predicate<Integer> match) {
        this.strategy = strategy;
        this.match = match;
    }

    public static AgeStrategy strategy(LoginMember loginMember) {
        if (loginMember.isAnonymous()) {
            return DEFAULT.strategy;
        }
        return Arrays.stream(values())
                .filter(s -> s.match.test(loginMember.getAge()))
                .findAny()
                .map(s -> s.strategy)
                .orElse(DEFAULT.strategy);
    }
}
