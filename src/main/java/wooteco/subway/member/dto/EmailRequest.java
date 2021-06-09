package wooteco.subway.member.dto;

import javax.validation.constraints.Email;

public class EmailRequest {

    @Email(message = "이메일 형식이 아닙니다.")
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
