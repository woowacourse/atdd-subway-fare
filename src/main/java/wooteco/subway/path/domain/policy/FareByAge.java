package wooteco.subway.path.domain.policy;

import java.util.Arrays;
import java.util.function.Predicate;

import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.policy.age.AdultPolicy;
import wooteco.subway.path.domain.policy.age.AgePolicy;
import wooteco.subway.path.domain.policy.age.BabyPolicy;
import wooteco.subway.path.domain.policy.age.ChildrenPolicy;
import wooteco.subway.path.domain.policy.age.TeenagerPolicy;

public enum FareByAge {
    BABY(new BabyPolicy(), (age) -> age < 6),
    CHILDREN(new ChildrenPolicy(), (age) -> age >= 6 && age < 13),
    TEENAGER(new TeenagerPolicy(), (age) -> age >= 13 && age < 19),
    ADULT(new AdultPolicy(), (age) -> age >= 19);

    private final AgePolicy agePolicy;
    private final Predicate<Integer> match;

    FareByAge(AgePolicy agePolicy, Predicate<Integer> match) {
        this.agePolicy = agePolicy;
        this.match = match;
    }

    public static int calculate(int fare, LoginMember loginMember) {
        return Arrays.stream(values())
            .filter(fareByAge -> fareByAge.match.test(loginMember.getAge()))
            .map(fareByAge -> fareByAge.agePolicy.calculate(fare))
            .findAny()
            .orElseThrow(RuntimeException::new);
    }
}
