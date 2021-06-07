package wooteco.subway.exception.notfound;

public class NotExistException extends NotFoundException {

    private static final String MESSAGE = "존재하지 않는 데이터입니다.";

    public NotExistException() {
        super(MESSAGE);
    }
}
