package wooteco.subway.member.domain;

public interface AuthMember {
    int discountFareByAge(int fare);
    boolean isLoggedIn();
}
