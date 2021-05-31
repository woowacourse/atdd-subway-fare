package wooteco.subway.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class EmailRequest {
    @NotBlank(message = "이메일에 공백만 있을 수 없습니다.")
    @Length(min = 4, max = 20, message = "이메일은 4글자 이상 20글자 이하여야합니다.")
    @Email(message = "이메일 형식에 맞춰서 작성해야 합니다. (ex. eamil@email.com)")
    private String email;

    public EmailRequest() {

    }

    public EmailRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
