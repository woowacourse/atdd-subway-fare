package wooteco.subway.exception;

public class DuplicatedStationNameException extends RuntimeException {

    private final static String DUPLICATED_NAME_EXCEPTION = "같은 이름으로 등록된 역이 있습니다.";

    public DuplicatedStationNameException() {
        super(DUPLICATED_NAME_EXCEPTION);
    }
}
