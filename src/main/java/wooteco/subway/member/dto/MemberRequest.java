package wooteco.subway.member.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

import wooteco.subway.member.domain.Member;

public class MemberRequest {
    @Pattern(regexp = "^[a-zA-Z0-9._%+]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]*$", message = "INVALID_EMAIL")
    @NotNull(message = "INVALID_EMAIL")
    private String email;

    @NotBlank(message = "INVALID_PASSWORD")
    private String password;

    @Positive(message = "INVALID_AGE")
    @NotNull(message = "INVALID_AGE")
    private Integer age;

    public MemberRequest() {
    }

    public MemberRequest(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Member toMember() {
        return new Member(email, password, age);
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
