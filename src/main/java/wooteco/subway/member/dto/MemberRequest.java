package wooteco.subway.member.dto;

import wooteco.subway.member.domain.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;

public class MemberRequest {
    private static final String INVALID_MEMBER_REQUEST_MESSAGE = "양식에 맞지 않는 회원가입 요청입니다.";

    @NotBlank(message = INVALID_MEMBER_REQUEST_MESSAGE)
    @Email(message = INVALID_MEMBER_REQUEST_MESSAGE)
    private String email;

    @NotBlank(message = INVALID_MEMBER_REQUEST_MESSAGE)
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = INVALID_MEMBER_REQUEST_MESSAGE)
    private String password;

    @Positive(message = INVALID_MEMBER_REQUEST_MESSAGE)
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
