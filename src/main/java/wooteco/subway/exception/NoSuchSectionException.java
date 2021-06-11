package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class NoSuchSectionException extends SubwayException {

    public NoSuchSectionException() {
        super("입력된 지하철 구간을 찾을 수 없습니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "NO_SUCH_SECTION";
    }
}
