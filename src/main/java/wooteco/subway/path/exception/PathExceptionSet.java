package wooteco.subway.path.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayExceptionSetInterface;

public enum PathExceptionSet implements SubwayExceptionSetInterface {

    AGE_NOT_FOUND("나이를 찾을 수 없습니다.", HttpStatus.NOT_FOUND.value());

    private final String message;
    private final int status;

    PathExceptionSet(String message, int status) {
        this.message = message;
        this.status = status;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public int status() {
        return status;
    }
}
