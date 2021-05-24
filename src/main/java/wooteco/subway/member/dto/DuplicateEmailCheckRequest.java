package wooteco.subway.member.dto;

public class DuplicateEmailCheckRequest {
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
