package wooteco.subway.exception;

public class NotFoundException extends RuntimeException{
    private static final String NOT_FOUND_MESSAGE_FORMAT = "%s(이)가 존재하지 않습니다.";
    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(String.format(NOT_FOUND_MESSAGE_FORMAT, message));
    }
}
