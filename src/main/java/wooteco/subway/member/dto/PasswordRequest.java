package wooteco.subway.member.dto;

public class PasswordRequest {
    private String currentPassword;
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
