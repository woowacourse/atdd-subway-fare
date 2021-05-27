package wooteco.subway.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import wooteco.subway.member.domain.Member;

public class MemberRequest {
    @Email(message = "이메일 형식에 맞지 않습니다.")
    @NotNull(message = "이메일은 공백이 아닙니다.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]*$", message = "이메일에는 특수문자가 포함될 수 없습니다.")
    private String email;

    @NotNull(message = "비밀번호는 공백이 아닙니다.")
    private String password;

    @Min(value = 0, message = "나이는 0세 이상부터 입력할 수 있습니다.")
    @NotNull(message = "나이는 공백이 아닙니다.")
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
