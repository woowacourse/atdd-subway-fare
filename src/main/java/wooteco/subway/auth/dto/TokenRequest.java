package wooteco.subway.auth.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class TokenRequest {

    @NotBlank(message = "INVALID_EMAIL")
    @Pattern(regexp = "^[a-zA-Z0-9._%+]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]*$", message = "INVALID_EMAIL")
    private String email;

    @NotBlank(message = "INVALID_PASSWORD")
    private String password;

    public TokenRequest() {
    }

    public TokenRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
