package wooteco.subway.member.domain;

public class LoginUser extends User {

    public LoginUser() {

    }
    public LoginUser(Long id, String email, Integer age) {
        super(id, email, age);
    }

    @Override
    public boolean isLogin() {
        return true;
    }
}
