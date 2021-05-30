package wooteco.subway.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import wooteco.subway.member.domain.Member;

public class MemberRequest {

    @NotNull(message = "INVALID_EMAIL")
    @Email(message = "INVALID_EMAIL")
    private String email;
    @NotBlank(message = "INVALID_PASSWORD")
    private String password;
    @NotNull(message = "INVALID_AGE")
    @PositiveOrZero(message = "INVALID_AGE")
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
