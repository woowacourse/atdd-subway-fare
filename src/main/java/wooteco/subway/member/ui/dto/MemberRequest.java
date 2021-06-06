package wooteco.subway.member.ui.dto;

import java.beans.ConstructorProperties;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import wooteco.subway.member.domain.Member;

public class MemberRequest {

    @Email(message = "이메일 형식이 잘못되었습니다.")
    @NotBlank(message = "이메일은 공백이 올 수 없습니다.")
    private final String email;

    @NotBlank(message = "패스워드는 공백이 올 수 없습니다.")
    @Length(min = 4, max = 20, message = "패스워드는 4 이상 20 이하의 길이로 작성해야 합니다.")
    private final String password;

    @Min(value = 1, message = "나이는 1보다 작을 수 없습니다.")
    @Max(value = 200, message = "나이는 200보다 클 수 없습니다.")
    private final Integer age;

    @ConstructorProperties({"email", "password", "age"})
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
