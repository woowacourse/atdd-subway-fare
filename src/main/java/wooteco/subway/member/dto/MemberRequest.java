package wooteco.subway.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import wooteco.subway.member.domain.Member;

public class MemberRequest {
    @Email(message = "이메일 형식만 가능합니다")
    private String email;
    @NotBlank(message = "입력되지 않은 항목을 확인해주세요")
    private String password;
    @Min(value = 0, message = "1부터 150 사이의 나이를 입력해주세요")
    @Max(value = 150, message = "1부터 150 사이의 나이를 입력해주세요")
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
