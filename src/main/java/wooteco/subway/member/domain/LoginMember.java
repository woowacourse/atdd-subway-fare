package wooteco.subway.member.domain;

public class LoginMember extends AuthMember {

    public LoginMember() {
        super();
    }

    public LoginMember(Long id, String email, Integer age) {
        super(id, email, age);
    }

    @Override
    public boolean isLoggedIn() {
        return true;
    }
}
