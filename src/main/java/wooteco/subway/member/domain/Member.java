package wooteco.subway.member.domain;

import org.apache.commons.lang3.StringUtils;
import wooteco.subway.exception.AuthorizationException;

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

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    @Override
    public boolean isGuest() {
        return false;
    }

    public void checkPassword(String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw new AuthorizationException();
        }
    }
}
