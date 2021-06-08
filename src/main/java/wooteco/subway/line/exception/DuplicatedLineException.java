package wooteco.subway.line.exception;

import wooteco.subway.line.dto.LineRequest;

public class DuplicatedLineException extends RuntimeException {

    public DuplicatedLineException(LineRequest request) {
        super(String.format("색상 혹은 이름이 이미 존재합니다. 이름 : %s, 색상 : %s", request.getName(), request.getColor()));
    }
}
