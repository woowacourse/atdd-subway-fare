package wooteco.subway.line.exception;

import wooteco.subway.exception.NotFoundException;

public final class NoLineException extends NotFoundException {
    public NoLineException() {
        super("존재하지 않는 노선입니다.");
    }
}
