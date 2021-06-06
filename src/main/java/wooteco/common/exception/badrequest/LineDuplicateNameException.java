package wooteco.common.exception.badrequest;

public class LineDuplicateNameException extends BadRequestException {
    public LineDuplicateNameException(String name) {
        super(name + "은 이미 존재하는 노선 이름입니다.");
    }
}