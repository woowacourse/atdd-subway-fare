package wooteco.subway.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import wooteco.subway.member.domain.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Getter
@NoArgsConstructor
public class MemberRequest {
    @Email(message = "INVALID_INPUT")
    private String email;
    @NotBlank(message = "INVALID_INPUT")
    private String password;
    @PositiveOrZero(message = "INVALID_INPUT")
    private Integer age;

    public MemberRequest(String email, String password, Integer age) {
        this.email = email;
        this.password = password;
        this.age = age;
    }

    public Member toMember() {
        return new Member(email, password, age);
    }
}
