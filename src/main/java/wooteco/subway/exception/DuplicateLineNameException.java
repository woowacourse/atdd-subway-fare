package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class DuplicateLineNameException extends SubwayException {

    public DuplicateLineNameException() {
        super("중복된 지하철 노선 이름이 존재합니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "DUPLICATED_LINE_NAME";
    }
}
