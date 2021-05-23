package wooteco.subway.member.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class PasswordRequest {
    @NotBlank
    private String currentPassword;
    @NotBlank
    private String newPassword;

    public PasswordRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}
