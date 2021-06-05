package wooteco.subway.exception.notfound;

public class InvalidPathException extends NotFoundException {
    public InvalidPathException() {
        super("해당되는 최단 경로를 찾을 수 없습니다.");
    }
}
