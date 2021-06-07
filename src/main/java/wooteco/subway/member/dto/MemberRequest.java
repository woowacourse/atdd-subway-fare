package wooteco.subway.member.dto;

import wooteco.subway.member.domain.Member;

import javax.validation.constraints.*;

public class MemberRequest {
    @Email(regexp = "^[0-9a-zA-Z]+([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]+([-_.]*[0-9a-zA-Z])*.[a-zA-Z]{2,3}$",
            message = "지원되지 않는 언어, 공백, 특수문자는 입력 불가능합니다.")
    @NotNull(message = "지원되지 않는 언어, 공백, 특수문자는 입력 불가능합니다.")
    @NotBlank(message = "지원되지 않는 언어, 공백, 특수문자는 입력 불가능합니다.")
    @Size(max = 30, message = "이메일 전체 글자 수는 30글자를 넘을 수 없습니다.")
    private String email;
    @Size(min = 4, max = 20, message = "비밀번호는 4글자 이상 20글자 이하이어야 합니다.")
    private String password;
    @Positive(message = "나이는 음수일 수 없습니다.")
    @Max(value = 200, message = "나이는 200살을 넘을 수 없습니다.")
    private int age;

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
