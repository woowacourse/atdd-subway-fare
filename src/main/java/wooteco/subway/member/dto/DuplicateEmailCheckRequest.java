package wooteco.subway.member.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class DuplicateEmailCheckRequest {
    @NotNull(message = "입력되지 않은 항목을 확인해주세요")
    @NotBlank(message = "입력되지 않은 항목을 확인해주세요")
    @Email(message = "올바른 이메일 형식으로 입력해주세요")
    private String email;

    public DuplicateEmailCheckRequest() {
    }

    public DuplicateEmailCheckRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
