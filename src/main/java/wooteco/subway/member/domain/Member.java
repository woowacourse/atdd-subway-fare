package wooteco.subway.member.domain;

import org.apache.commons.lang3.StringUtils;
import wooteco.subway.exception.AuthorizationException;

public class Member {
    private static final Long DEFAULT_ID = 0L;

    private Long id;
    private String email;
    private String password;
    private Age age;

    public Member() {
    }

    public Member(Long id, String email, String password, Age age) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Member(Long id, String email, String password, Integer age) {
        this(id, email, password, Age.of(age));
    }

    public Member(String email, String password, Integer age) {
        this(DEFAULT_ID, email, password, Age.of(age));
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
        return age.getAge();
    }

    public void checkPassword(String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw new AuthorizationException();
        }
    }
}
