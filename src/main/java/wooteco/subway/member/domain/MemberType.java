package wooteco.subway.member.domain;

import com.google.common.base.Predicate;

import java.util.Arrays;
import java.util.Optional;

public enum MemberType {
    BABY((age) -> age < 6),
    CHILD((age) -> age >= 6 && age < 13),
    ADOLESCENT((age) -> age >= 13 && age < 19),
    ADULT((age) -> age >= 19),
    NONE;

    Predicate<Integer> predicate = (i) -> false;

    MemberType() {
    }

    MemberType(Predicate<Integer> predicate) {
        this.predicate = predicate;
    }

    public static MemberType of(Optional<LoginMember> member) {
        if (!member.isPresent()) {
            return NONE;
        }

        int age = member.get()
                        .getAge();
        return Arrays.stream(values())
                     .filter(memberType -> memberType.is(age))
                     .findFirst()
                     .orElse(NONE);
    }

    private boolean is(int age) {
        return this.predicate.apply(age);
    }
}
