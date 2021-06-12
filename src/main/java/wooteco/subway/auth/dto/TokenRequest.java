package wooteco.subway.auth.dto;

import javax.validation.constraints.NotBlank;

public class TokenRequest {
    @NotBlank(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "패스워드는 빈 칸이 될 수 없습니다.")
    private String password;

    public TokenRequest() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
