package wooteco.subway.member.dto;

import wooteco.subway.member.domain.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

public class MemberRequest {
    @NotEmpty
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    private String email;

    @NotEmpty(message = "유효하지 않은 패스워드입니다.")
    private String password;

    @NotEmpty
    @Positive(message = "나이는 양수여야 합니다.")
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
