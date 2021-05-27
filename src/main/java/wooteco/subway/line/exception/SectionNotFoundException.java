package wooteco.subway.line.exception;

import wooteco.subway.exception.NotFoundException;

public class SectionNotFoundException extends NotFoundException {
    private static final String SECTION_NOT_FOUND_MESSAGE = "%s의 구간";
    public SectionNotFoundException() {
    }

    public SectionNotFoundException(String message) {
        super(String.format(SECTION_NOT_FOUND_MESSAGE, message));
    }
}
