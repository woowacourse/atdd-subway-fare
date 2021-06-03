package wooteco.subway.path.exception;

import wooteco.subway.exception.NotFoundException;

public final class InvalidPathException extends NotFoundException {
    public InvalidPathException() {
        super("해당되는 최단 경로를 찾을 수 없습니다.");
    }
}
