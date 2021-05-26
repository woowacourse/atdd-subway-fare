package wooteco.subway.exception.deletion;

public class StationCannotDeleteException extends CannotDeleteException {
    private static final String MESSAGE = "이미 노선에 등록된 역은 삭제할 수 없습니다.";

    public StationCannotDeleteException() {
        super(MESSAGE);
    }
}
