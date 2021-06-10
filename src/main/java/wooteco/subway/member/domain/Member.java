package wooteco.subway.member.domain;

import org.apache.commons.lang3.StringUtils;
import wooteco.subway.auth.application.AuthorizationException;
import wooteco.subway.member.application.MemberException;

import java.util.Objects;

public class Member {
    private Long id;
    private String email;
    private String password;
    private Integer age;

    public Member() {
    }

    public Member(Long id, String email, String password, Integer age) {
        validate(email, password, age);

        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Member(String email, String password, Integer age) {
        this(null, email, password, age);
    }

    private void validate(String email, String password, Integer age) {
        if (Objects.isNull(age) || age < 0) {
            throw new MemberException("나이는 음수가 될 수 없습니다.");
        }

        if (Objects.isNull(email) || email.isEmpty()) {
            throw new MemberException("회원 이메일은 빈 문자열일 수 없습니다.");
        }

        if (Objects.isNull(password) || password.isEmpty()) {
            throw new MemberException("회원 비밀번호는 빈 문자열일 수 없습니다.");
        }
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
            throw new AuthorizationException("패스워드가 일치하지 않습니다.");
        }
    }

    public boolean hasSameEmail(final String email) {
        return this.email.equals(email);
    }
}
