package wooteco.subway.member.domain;

import org.apache.commons.lang3.StringUtils;

public class Member {
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

    public Member(Long id, String email, Age age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public Member(String email, String password, Age age) {
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

    public Integer getAgeAsInt() {
        return age.getAge();
    }

    public Age getAge() {
        return age;
    }

    public void checkPassword(String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw new WrongPasswordException();
        }
    }
}
