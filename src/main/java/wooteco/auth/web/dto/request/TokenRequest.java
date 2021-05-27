package wooteco.auth.web.dto.request;

import wooteco.auth.web.dto.Password;

import javax.validation.constraints.Email;

public class TokenRequest {
    @Email
    private String email;
    @Password
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
