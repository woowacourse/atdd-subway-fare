package wooteco.subway.exception.line;

import wooteco.subway.exception.SubwayException;

public class DuplicateNameException extends SubwayException {
    public DuplicateNameException() {
        super("지하철 노선 이름이 이미 존재합니다");
    }
}
