package wooteco.subway.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class TokenRequest {
    @Email(message = "INVALID_INPUT")
    private String email;
    @NotBlank(message = "INVALID_INPUT")
    private String password;

    public TokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
