package wooteco.subway.exception;

public class InvalidLineException extends InvalidException {

    public InvalidLineException() {
        super("존재하지 않는 노선입니다.");
    }

}
