package wooteco.subway.member.dto;

import javax.validation.constraints.Email;

public class EmailRequest {
    private static final String INVALID_EMAIL_REQUEST_MESSAGE = "양식에 맞지 않는 이메일 검증 요청입니다.";

    @Email(message = INVALID_EMAIL_REQUEST_MESSAGE)
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
