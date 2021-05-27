package wooteco.subway.line.application;

public class NotAbleToDeleteInSectionException extends IllegalArgumentException {
    private static final String ERROR_MESSAGE = "구간이 하나인 경우 역 삭제 불가능합니다.";

    public NotAbleToDeleteInSectionException() {
        super(ERROR_MESSAGE);
    }
}
