package wooteco.subway.member.dto;

public class EmailExistsResponse {
    private Boolean exists;

    public EmailExistsResponse() {
    }

    public EmailExistsResponse(Boolean isExist) {
        this.exists = isExist;
    }

    public Boolean getExist() {
        return exists;
    }
}
