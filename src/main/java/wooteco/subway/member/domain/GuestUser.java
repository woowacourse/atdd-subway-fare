package wooteco.subway.member.domain;

public class GuestUser extends User {

    @Override
    public boolean isLogin() {
        return false;
    }
}
