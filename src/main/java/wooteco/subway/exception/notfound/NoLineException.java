package wooteco.subway.exception.notfound;

public class NoLineException extends NotFoundException {
    public NoLineException() {
        super("존재하지 않는 노선입니다.");
    }
}
