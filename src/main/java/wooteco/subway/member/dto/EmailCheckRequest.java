package wooteco.subway.member.dto;

public class EmailCheckRequest {
    private String email;

    public EmailCheckRequest() {

    }

    public EmailCheckRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
