package wooteco.subway.member.dto;

import javax.validation.constraints.NotBlank;

public class MemberPasswordRequest {
    @NotBlank
    private String currentPassword;
    @NotBlank
    private String newPassword;

    public MemberPasswordRequest() {
    }

    public MemberPasswordRequest(String currentPassword, String newPassword) {
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
