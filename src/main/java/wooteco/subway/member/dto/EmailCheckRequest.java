package wooteco.subway.member.dto;

import javax.validation.constraints.NotBlank;

public class EmailCheckRequest {
    @NotBlank
    private String email;

    public EmailCheckRequest() {
    }

    public EmailCheckRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
