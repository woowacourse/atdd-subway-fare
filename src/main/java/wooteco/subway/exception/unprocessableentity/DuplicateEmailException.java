package wooteco.subway.exception.unprocessableentity;

public class DuplicateEmailException extends UnprocessableEntityException {
    public DuplicateEmailException() {
        super("이메일이 중복되었습니다.");
    }
}
