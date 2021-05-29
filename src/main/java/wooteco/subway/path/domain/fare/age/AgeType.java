package wooteco.subway.path.domain.fare.age;

import java.util.Arrays;
import java.util.function.Predicate;

import static wooteco.subway.path.domain.fare.age.AgeType.Constants.ANONYMOUS_USER_AGE;

public enum AgeType {
    BABY(new BabyStrategy(), age -> age > 0 && age < 6),
    CHILD(new ChildStrategy(), age -> age >= 6 && age < 13),
    TEENAGER(new TeenagerStrategy(), age -> age >= 13 && age < 19),
    DEFAULT(new DefaultStrategy(), age -> age == ANONYMOUS_USER_AGE || age >= 19);

    private final AgeStrategy strategy;
    private final Predicate<Integer> match;

    AgeType(AgeStrategy strategy, Predicate<Integer> match) {
        this.strategy = strategy;
        this.match = match;
    }

    public static AgeStrategy ageStrategy(int age) {
        return Arrays.stream(values())
                .filter(s -> s.match.test(age))
                .findAny()
                .map(s -> s.strategy)
                .orElse(DEFAULT.strategy);
    }

    static class Constants {
        public static final int ANONYMOUS_USER_AGE = 0;
    }
}
