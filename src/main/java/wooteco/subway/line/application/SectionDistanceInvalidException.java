package wooteco.subway.line.application;

public class SectionDistanceInvalidException extends RuntimeException {
    private static final String ERROR_MESSAGE = "구간의 길이가 잘못되었습니다.";

    public SectionDistanceInvalidException() {
        super(ERROR_MESSAGE);
    }
}
