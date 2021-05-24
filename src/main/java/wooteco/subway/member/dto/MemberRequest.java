package wooteco.subway.member.dto;

import wooteco.subway.member.domain.Member;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

public class MemberRequest {

    @NotBlank(message = "이메일은 빈칸일 수 없습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 빈칸일 수 없습니다.")
    private String password;

    @Positive(message = "나이는 음수일 수 없습니다.")
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
