package wooteco.subway.member.domain.authmember;

public interface AuthMember {

    int discountFareByAge(int fare);

    boolean isLoggedIn();
}
