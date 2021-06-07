package wooteco.subway.member.domain;

public class LoginMember {
    private String email;

    public LoginMember() {
    }

    public LoginMember(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
