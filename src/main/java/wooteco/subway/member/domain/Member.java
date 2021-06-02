package wooteco.subway.member.domain;

import org.apache.commons.lang3.StringUtils;
import wooteco.subway.config.exception.AuthorizationException;

public class Member {

    private Long id;
    private String email;
    private String password;
    private Age age;

    public Member() {
    }

    public Member(String email, String password, Integer age) {
        this(null, email, password, age);
    }

    public Member(Long id, String email, String password, Integer age) {
        this(id, email, password, new Age(age));
    }

    public Member(Long id, String email, String password, Age age) {
        this.id = id;
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
        return age.toInt();
    }

    public void checkPassword(String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw new AuthorizationException("비밀번호가 다릅니다");
        }
    }
}
