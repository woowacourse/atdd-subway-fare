package wooteco.subway.member.dto;

import javax.validation.constraints.Email;

public class EmailExistsRequest {

    @Email
    private String email;

    public EmailExistsRequest() {
    }

    public EmailExistsRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
