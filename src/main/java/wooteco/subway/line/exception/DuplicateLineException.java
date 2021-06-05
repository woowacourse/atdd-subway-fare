package wooteco.subway.line.exception;

import wooteco.subway.exception.BadRequestException;

public final class DuplicateLineException extends BadRequestException {
    public DuplicateLineException() {
        super("중복된 지하철 노선입니다.");
    }
}
