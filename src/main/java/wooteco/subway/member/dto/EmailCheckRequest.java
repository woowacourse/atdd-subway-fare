package wooteco.subway.member.dto;

import javax.validation.constraints.Email;

public class EmailCheckRequest {
    @Email
    private String email;

    public EmailCheckRequest() {
    }

    public EmailCheckRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
