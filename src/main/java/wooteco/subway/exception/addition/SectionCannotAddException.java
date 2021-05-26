package wooteco.subway.exception.addition;

public class SectionCannotAddException extends CannotAddException {
    private static final String MESSAGE = "구간 추가에 필요한 정보가 잘못되었습니다.";

    public SectionCannotAddException() {
        super(MESSAGE);
    }
}
