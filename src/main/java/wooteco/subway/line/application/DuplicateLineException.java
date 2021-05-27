package wooteco.subway.line.application;

import wooteco.subway.SubwayException;

public class DuplicateLineException extends SubwayException {
    public DuplicateLineException() {
        this("라인이 중복되었습니다람쥐.");
    }

    public DuplicateLineException(String message) {
        super(message, "Duplication");
    }
}
