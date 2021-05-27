package wooteco.subway.member.dto;

import wooteco.subway.member.domain.Age;
import wooteco.subway.member.domain.Member;

import javax.validation.constraints.*;

public class MemberRequest {
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;
    @NotBlank(message = "비밀번호는 공백일 수 없습니다")
    @Size(min = 4, max = 20, message = "비밀번호는 4자리 에서 20자리 사이여야 합니다.")
    private String password;
    @Max(value = 200, message = "나이는 200 초과 일 수 없습니다.")
    @Min(value = 1, message = "나이는 1 미만일 수 없습니다.")
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
        return new Member(email, password, new Age(age));
    }
}
