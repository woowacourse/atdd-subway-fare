package wooteco.subway.exception;

public class DuplicatedEmailException extends SubwayException {
    private static final String ERROR = "DUPLICATED_ID";
    private static final String MESSAGE = "중복되는 이메일이 존재합니다.";

    public DuplicatedEmailException() {
        super(ERROR, MESSAGE);
    }
}
