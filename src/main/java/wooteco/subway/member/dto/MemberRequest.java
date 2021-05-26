package wooteco.subway.member.dto;

import wooteco.subway.member.domain.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class MemberRequest {
    @Email(message = "올바른 이메일 형식으로 입력해주세요")
    @NotBlank(message = "입력되지 않은 항목을 확인해주세요")
    private String email;
    @NotBlank(message = "입력되지 않은 항목을 확인해주세요")
    private String password;
    @NotNull(message = "입력되지 않은 항목을 확인해주세요") @Positive(message = "1부터 150사이의 나이를 입력해주세요.")
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
