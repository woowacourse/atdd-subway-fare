package wooteco.subway.member.domain;

import static wooteco.subway.exception.SubwayExceptions.*;

import org.apache.commons.lang3.StringUtils;

import wooteco.subway.member.dto.MemberRequest;

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
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Member(Long id, MemberRequest memberRequest) {
        this(id, memberRequest.getEmail(), memberRequest.getPassword(), memberRequest.getAge());
    }

    public void update(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public void checkPassword(String password) {
        if (!StringUtils.equals(this.password, password)) {
            throw INVALID_PASSWORD.makeException();
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
}
