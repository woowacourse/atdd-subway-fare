package wooteco.subway.member.domain;

import org.apache.commons.lang3.StringUtils;
import wooteco.subway.exception.AuthorizationException;
import wooteco.subway.exception.InvalidInsertException;

public class Member implements User {
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

    public void checkPassword(String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw new AuthorizationException("이메일 혹은 비밀번호를 다시 확인해주세요");
        }
    }

    public void validatePassword(String currentPassword, String newPassword) {
        if (!this.password.equals(currentPassword)) {
            throw new InvalidInsertException("현재 비밀번호를 다시 확인해주세요");
        }
        if (currentPassword.equals(newPassword)) {
            throw new InvalidInsertException("현재 사용 중인 비밀번호입니다. 다른 비밀번호를 입력해주세요");
        }
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Integer getAge() {
        return age;
    }

    @Override
    public boolean isGuest() {
        return false;
    }
}
