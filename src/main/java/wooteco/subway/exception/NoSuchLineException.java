package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class NoSuchLineException extends SubwayException {

    public NoSuchLineException() {
        super("입력된 지하철 노선을 찾을 수 없습니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "NO_SUCH_LINE";
    }
}
