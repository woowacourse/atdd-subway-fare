package wooteco.subway.member.domain;

public class NotLoginMember implements AuthMember {

    @Override
    public int discountFareByAge(int fare) {
        return fare;
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }
}
