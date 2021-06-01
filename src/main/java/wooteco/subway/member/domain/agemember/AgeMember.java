package wooteco.subway.member.domain.agemember;

public interface AgeMember {

    boolean isInAgeRange(int age);

    int discountFareByAge(int fare);
}
