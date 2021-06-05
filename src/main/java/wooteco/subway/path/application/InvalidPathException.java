package wooteco.subway.path.application;

import wooteco.subway.SubwayException;

public class InvalidPathException extends SubwayException {
    public InvalidPathException() {
        super("빈 구간 그래프입니다.", "Empty");
    }
}
