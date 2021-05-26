package wooteco.subway.exception;

public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException() {
        super("존재하지 않는 데이터입니다.");
    }

    public DataNotFoundException(String message) {
        super(message);
    }
}
