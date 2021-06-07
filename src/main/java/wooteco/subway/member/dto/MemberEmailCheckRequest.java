package wooteco.subway.member.dto;

public class MemberEmailCheckRequest {

    private String email;

    public MemberEmailCheckRequest(String email) {
        this.email = email;
    }

    public MemberEmailCheckRequest() {
    }

    public String getEmail() {
        return email;
    }
}
