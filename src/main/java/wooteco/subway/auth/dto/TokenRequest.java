package wooteco.subway.auth.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class TokenRequest {
    @NotEmpty(message = "입력되지 않은 항목을 확인해주세요")
    @Email(message = "올바른 이메일 형식으로 입력해주세요")
    private String email;

    @NotEmpty(message = "입력되지 않은 항목을 확인해주세요")
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
