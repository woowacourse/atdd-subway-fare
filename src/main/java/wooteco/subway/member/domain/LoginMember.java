package wooteco.subway.member.domain;

import static wooteco.subway.member.domain.MinimumAge.*;

public class LoginMember {
    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginMember of(String email) {
        return new LoginMember(null, email, null);
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

    public boolean isAdult() {
        return this.age >= ADULT.getValue();
    }

    public boolean isTeenager() {
        return this.age >= TEENAGER.getValue();
    }

    public boolean isChild() {
        return this.age >= CHILD.getValue();
    }
}
