package wooteco.subway.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;

@Getter
@NoArgsConstructor
public class EmailRequest {
    @Email(message = "INVALID_INPUT")
    private String email;

    public EmailRequest(String email) {
        this.email = email;
    }
}
