package wooteco.subway.exception.line;

import wooteco.subway.exception.SubwayException;

public class DuplicateColorException extends SubwayException {
    public DuplicateColorException() {
        super("지하철 노선 색깔이 이미 존재합니다");
    }
}
