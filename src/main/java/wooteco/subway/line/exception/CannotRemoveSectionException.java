package wooteco.subway.line.exception;

import wooteco.subway.exception.BadRequestException;

public final class CannotRemoveSectionException extends BadRequestException {
    public CannotRemoveSectionException() {
        super("구간이 하나인 노선에서는 구간을 삭제할 수 없습니다.");
    }
}
