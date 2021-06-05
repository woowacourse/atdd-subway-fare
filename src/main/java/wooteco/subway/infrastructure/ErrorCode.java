package wooteco.subway.infrastructure;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "유효하지 않은 입력값 입니다."),
    NOT_EXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String error;

    ErrorCode(final HttpStatus httpStatus, final String error) {
        this.httpStatus = httpStatus;
        this.error = error;
    }


    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getError() {
        return error;
    }
}
