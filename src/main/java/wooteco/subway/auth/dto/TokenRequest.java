package wooteco.subway.auth.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public class TokenRequest {

    @NotBlank(message = "이메일은 공백이 아닙니다.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]*$", message = "이메일에는 특수문자가 포함될 수 없습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 공백일 수 없습니다.")
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
