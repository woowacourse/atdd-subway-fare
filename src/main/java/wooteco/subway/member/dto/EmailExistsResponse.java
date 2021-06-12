package wooteco.subway.member.dto;

public class EmailExistsResponse {
    private Boolean exists;

    public EmailExistsResponse(Boolean isExist) {
        this.exists = isExist;
    }

    public EmailExistsResponse() {
    }

    public Boolean getExist() {
        return exists;
    }
}
