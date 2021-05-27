package wooteco.common.exception.badrequest;

public class LineDuplicateColorException extends BadRequestException {
    public LineDuplicateColorException(String color) {
        super(color + "은 이미 존재하는 노선 색입니다.");
    }
}