package wooteco.subway.member.ui.dto;

import java.beans.ConstructorProperties;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;
import wooteco.subway.member.domain.Member;

public class MemberRequest {

    @Email
    @NotBlank
    private final String email;

    @NotBlank
    @Length(min = 4, max = 20)
    private final String password;

    @Min(1)
    @Max(200)
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
