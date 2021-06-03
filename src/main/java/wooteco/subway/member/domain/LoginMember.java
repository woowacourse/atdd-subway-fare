package wooteco.subway.member.domain;

public class LoginMember extends User {
    public LoginMember(Long id, String email, Integer age) {
        super(id, email, age);
    }

    @Override
    public boolean isLoginMember() {
        return true;
    }
}
