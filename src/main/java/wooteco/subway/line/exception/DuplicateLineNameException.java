package wooteco.subway.line.exception;

public class DuplicateLineNameException extends LineDaoException {
    public static final String MESSAGE = "이미 존재하는 노선 이름입니다.";

    public DuplicateLineNameException() {
        super(MESSAGE);
    }
}
