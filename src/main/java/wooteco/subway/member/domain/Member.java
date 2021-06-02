package wooteco.subway.member.domain;

import org.apache.commons.lang3.StringUtils;
import wooteco.subway.exception.auth.WrongPasswordException;

public class Member {
    private final Long id;
    private final String email;
    private final String password;
    private final Integer age;

    public Member(Long id, String email, String password, Integer age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Member(Long id, String email, Integer age) {
        this(id, email, null, age);
    }

    public Member(String email, String password, Integer age) {
        this(null, email, password, age);
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
            throw new WrongPasswordException();
        }
    }
}
