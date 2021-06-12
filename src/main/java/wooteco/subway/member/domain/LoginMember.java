package wooteco.subway.member.domain;

import wooteco.subway.auth.application.AuthorizationException;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
    }

    public LoginMember(User user) {
        this.id = user.getId();
        this.age = user.getAge();
        this.email = user.getEmail();
    }

    public static LoginMember of(User user) {
        if (!user.isLoggedIn()) {
            throw new AuthorizationException("로그인된 유저가 아닙니다.");
        }
        return new LoginMember(user);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }
}
