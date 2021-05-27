package wooteco.auth.web.dto.request;

import javax.validation.constraints.Email;
import wooteco.auth.web.dto.Password;

public class TokenRequest {
    @Email
    private String email;
    @Password(minSize = 8, maxSize = 14)
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
