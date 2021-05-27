package wooteco.subway.exception;

public class OnlyOneSectionExistException extends SubwayException {
    private static final String ERROR = "ONLY_ONE_SECTION_EXIST";
    private static final String MESSAGE = "오직 하나의 구간이 남아 삭제할 수 없습니다.";

    public OnlyOneSectionExistException() {
        super(ERROR, MESSAGE);
    }
}
