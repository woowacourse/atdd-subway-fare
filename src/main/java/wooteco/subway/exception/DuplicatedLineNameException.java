package wooteco.subway.exception;

public class DuplicatedLineNameException extends DuplicatedNameException {

    private final static String DUPLICATED_NAME_EXCEPTION = "같은 이름으로 등록된 노선이 있습니다.";

    public DuplicatedLineNameException() {
        super(DUPLICATED_NAME_EXCEPTION);
    }
}
