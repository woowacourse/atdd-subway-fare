package wooteco.subway.line.exception;

public class SectionRemovalException  extends RuntimeException {
    public static final String ERROR_MESSAGE = "구간 삭제를 할 수 없습니다.";

    public SectionRemovalException() {
        super(ERROR_MESSAGE);
    }
}
