package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class SameStationsInSameSectionException extends SubwayException {

    public SameStationsInSameSectionException() {
        super("구간 등록에 서로 같은 역을 입력할 수 없습니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "SAME_STATIONS_IN_SAME_SECTION";
    }
}
