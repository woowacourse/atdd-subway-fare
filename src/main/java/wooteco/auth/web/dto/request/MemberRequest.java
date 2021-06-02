package wooteco.auth.web.dto.request;

import wooteco.auth.domain.Member;
import wooteco.auth.web.dto.validator.Password;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class MemberRequest {
    @Email
    private String email;
    @Password(minSize = 8, maxSize = 14)
    private String password;
    @Positive
    @NotNull
    private Integer age;

    public MemberRequest() {
    }

    public MemberRequest(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
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

    public Member toMember() {
        return new Member(email, password, age);
    }
}
