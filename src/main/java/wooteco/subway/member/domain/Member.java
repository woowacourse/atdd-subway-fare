package wooteco.subway.member.domain;

import org.apache.commons.lang3.StringUtils;
import wooteco.subway.auth.application.AuthorizationException;

public class Member {
    private Long id;
    private String email;
    private String password;
    private Integer age;

    public Member() {
    }

    public Member(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Member(String email, String password, Integer age) {
        this(null, email, password, age);
    }

    public void checkPassword(String password) {
        if (StringUtils.equals(this.password, password)) {
            return;
        }
        throw new AuthorizationException();
    }

    public boolean isSameEmail(String email) {
        return this.email.equals(email);
    }

    public void update(Integer age, String password) {
        this.age = age;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Integer getAge() {
        return age;
    }
}
