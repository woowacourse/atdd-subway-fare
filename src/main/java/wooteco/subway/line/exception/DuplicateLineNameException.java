package wooteco.subway.line.exception;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateLineNameException extends DuplicateKeyException {
    private static final String ERROR_MESSAGE = "이미 등록된 노선 이름입니다.";

    public DuplicateLineNameException() {
        super(ERROR_MESSAGE);
    }
}

