package wooteco.subway.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class EmailRequest {
    @NotEmpty
    @Email(message = "유효하지 않은 이메일 형식입니다.")
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
