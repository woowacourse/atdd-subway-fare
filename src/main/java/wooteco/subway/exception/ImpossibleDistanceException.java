package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public class ImpossibleDistanceException extends SubwayException {

    public ImpossibleDistanceException() {
        super("현재 노선에 등록되어 있는 구간보다 작은 거리로 등록할 수 없습니다.");
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String error() {
        return "IMPOSSIBLE_DISTANCE";
    }
}
