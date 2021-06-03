package wooteco.subway.line.exception;

import wooteco.subway.exception.BadRequestException;

public final class InvalidStationsOfSectionException extends BadRequestException {
    public InvalidStationsOfSectionException() {
        super("구간에 대한 지하철 역 입력이 잘못되었습니다.");
    }
}
