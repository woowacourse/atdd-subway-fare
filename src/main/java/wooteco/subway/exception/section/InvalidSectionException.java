package wooteco.subway.exception.section;

import wooteco.subway.exception.SubwayException;

public class InvalidSectionException extends SubwayException {
    public InvalidSectionException() {
        super("유효하지 않는 요청 값입니다");
    }
}
