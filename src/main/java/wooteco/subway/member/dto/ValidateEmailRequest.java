package wooteco.subway.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

public class ValidateEmailRequest {

    @NotNull(message = "INVALID_EMAIL")
    @Email(message = "INVALID_EMAIL")
    private String email;

    public ValidateEmailRequest() {
    }

    public ValidateEmailRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
