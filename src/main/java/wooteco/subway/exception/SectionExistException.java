package wooteco.subway.exception;

public class SectionExistException extends CannotAddSectionException {

    private static final String SECTION_EXIST = "이미 등록된 구간입니다.";

    public SectionExistException() {
        super(SECTION_EXIST);
    }
}
