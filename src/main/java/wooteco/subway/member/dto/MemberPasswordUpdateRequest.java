package wooteco.subway.member.dto;

public class MemberPasswordUpdateRequest {
    private String currentPassword;
    private String newPassword;

    public MemberPasswordUpdateRequest(String currentPassword, String newPassword) {
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
