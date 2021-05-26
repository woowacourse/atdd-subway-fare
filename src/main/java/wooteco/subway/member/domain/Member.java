package wooteco.subway.member.domain;

import org.apache.commons.lang3.StringUtils;
import wooteco.subway.exception.badrequest.LoginFailException;
import wooteco.subway.exception.badrequest.PasswordMissMatchException;
import wooteco.subway.exception.badrequest.UpdatePasswordException;

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
            throw new LoginFailException();
        }
    }

    public void validateUpdatePassword(String currentPassword, String newPassword) {
        if (!this.password.equals(currentPassword)) {
            throw new PasswordMissMatchException();
        }
        if (currentPassword.equals(newPassword)) {
            throw new UpdatePasswordException();
        }
    }

    public Member update(String email, String password, Integer age) {
        return new Member(this.id, email, password, age);
    }
}
