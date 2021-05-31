package wooteco.subway.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;
import wooteco.subway.member.domain.Member;

public class MemberRequest {

    @NotBlank(message = "이메일에 공백만 있을 수 없습니다.")
    @Length(min = 4, max = 20, message = "이메일은 4글자 이상 20글자 이하여야합니다.")
    @Email(message = "이메일 형식에 맞춰서 작성해야 합니다. (ex. eamil@email.com)")
    private String email;

    @NotEmpty(message = "패스워드에 공백만 있을 수 없습니다.")
    @Length(min = 4, max = 20, message = "패스워드는 4글자 이상 20글자 이하여야합니다.")
    @Pattern(regexp = "^[a-z|A-Z|0-9]*$", message = "패스워드는 영어 또는 숫자여야 합니다.")
    private String password;

    @NotNull
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
