package wooteco.subway.member.domain;

import com.google.common.base.Predicate;

import java.util.Arrays;

public enum MemberType {
    BABY((age) -> age < 6),
    CHILD((age) -> age >= 6 && age < 13),
    ADOLESCENT((age) -> age >= 13 && age < 19),
    ADULT((age) -> age >= 19),
    NONE;

    private Predicate<Integer> predicate = (i) -> false;

    MemberType() {
    }

    MemberType(Predicate<Integer> predicate) {
        this.predicate = predicate;
    }

    public static MemberType of(User user) {
        if (!user.isLoggedIn()) {
            return NONE;
        }

        int age = user.getAge();
        return Arrays.stream(values())
                .filter(memberType -> memberType.is(age))
                .findFirst()
                .orElse(NONE);
    }

    private boolean is(int age) {
        return this.predicate.apply(age);
    }
}
