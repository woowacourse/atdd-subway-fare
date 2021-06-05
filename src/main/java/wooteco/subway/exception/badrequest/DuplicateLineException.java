package wooteco.subway.exception.badrequest;

public class DuplicateLineException extends BadRequestException {
    public DuplicateLineException() {
        super("중복된 지하철 노선입니다.");
    }
}
