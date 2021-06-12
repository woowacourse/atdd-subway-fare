package wooteco.subway.member.domain;

public class GuestUser implements User {
    @Override
    public Integer getAge() {
        return null;
    }

    @Override
    public boolean isLogin() {
        return false;
    }
}
