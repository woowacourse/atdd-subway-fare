package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class OnlyOneSectionExistsException extends SubwayException {

    public OnlyOneSectionExistsException() {
        super("지하철 노선에 최소 1개 이상의 구간은 존재해야 합니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "ONLY_ONE_SECTION_EXISTS";
    }
}
