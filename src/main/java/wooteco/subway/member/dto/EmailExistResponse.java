package wooteco.subway.member.dto;

public class EmailExistResponse {

    private boolean exist;

    public EmailExistResponse(boolean exist) {
        this.exist = exist;
    }

    public boolean isExist() {
        return exist;
    }
}
