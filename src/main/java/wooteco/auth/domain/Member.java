package wooteco.auth.domain;

import org.apache.commons.lang3.StringUtils;
import wooteco.common.exception.forbidden.AuthorizationException;

public class Member {
    private static final String PASSWORD_ERROR_MESSAGE = "비밀번호가 일치하지 않습니다.";

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

    public Member(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public Member(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
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

    public void checkPassword(String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw new AuthorizationException(PASSWORD_ERROR_MESSAGE);
        }
    }
}
