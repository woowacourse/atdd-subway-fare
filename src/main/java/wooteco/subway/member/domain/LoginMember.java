package wooteco.subway.member.domain;

import java.util.Objects;
import wooteco.subway.exception.AuthorizationException;

public class LoginMember {

    private Id id;
    private Email email;
    private Age age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this(new Id(id), new Email(email), new Age(age));
    }

    public LoginMember(Id id, Email email, Age age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public void validateNull() {
        if (Objects.isNull(id)) {
            throw new AuthorizationException();
        }
    }

    public Long getId() {
        return id.getValue();
    }

    public String getEmail() {
        return email.getValue();
    }

    public Integer getAge() {
        return age.getValue();
    }
}
