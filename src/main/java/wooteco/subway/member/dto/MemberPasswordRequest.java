package wooteco.subway.member.dto;

public class MemberPasswordRequest {
    private String currentPassword;
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
