package wooteco.subway.line.exception;

import org.springframework.http.HttpStatus;
import wooteco.subway.exception.SubwayExceptionSetInterface;

public enum SectionExceptionSet implements SubwayExceptionSetInterface {

    INVALID_SECTION_DISTANCE_EXCEPTION("잘못된 노선 구간 거리입니다.", HttpStatus.BAD_REQUEST.value()),
    INVALID_SECTION_DATA_EXCEPTION("구간 추가에 필요한 정보가 잘못되었습니다.", HttpStatus.BAD_REQUEST.value()),
    ILLEGAL_SECTION_DELETE_EXCEPTION("노선에는 최소한 하나의 구간은 존재해야 합니다.", HttpStatus.BAD_REQUEST.value());

    private final String message;
    private final int status;

    SectionExceptionSet(String message, int status) {
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
