package wooteco.subway.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class MemberEmailRequest {
    @NotBlank
    @Email
    private String email;

    MemberEmailRequest() {
    }

    public MemberEmailRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
