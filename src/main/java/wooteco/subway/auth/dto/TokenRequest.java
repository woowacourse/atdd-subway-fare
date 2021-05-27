package wooteco.subway.auth.dto;

import javax.validation.constraints.Email;
import wooteco.subway.validator.SubwayPassword;

public class TokenRequest {

    @Email
    private String email;
    @SubwayPassword(min = 8, max = 14)
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
