package wooteco.subway.infrastructure;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    COMMON_INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "유효하지 않은 입력값 입니다."),
    COMMON_NOT_EXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에서 오류가 발생했습니다."),


    AUTH_NOT_VALID_LOGIN_INFO(HttpStatus.UNAUTHORIZED, "이메일과 비밀번호를 확인해 주세요."),
    AUTH_NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),


    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 회원 입니다."),
    MEMBER_EMAIL_DUPLICATED(HttpStatus.UNPROCESSABLE_ENTITY, "해당 이메일이 이미 존재 합니다."),

    LINE_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 노선 입니다."),
    LINE_DUPLICATED(HttpStatus.BAD_REQUEST, "중복된 지하철 노선입니다"),

    STATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "존재하지 않는 지하철 역 입니다."),
    STATION_DUPLICATED(HttpStatus.BAD_REQUEST, "이미 존재하는 지하철 역입니다."),

    SECTION_CREATE(HttpStatus.BAD_REQUEST, "구간 입력이 잘못 되었습니다."),
    SECTION_DELETE(HttpStatus.BAD_REQUEST, "구간에 존재하는 지하 역을 삭제할 수 없습니다.");

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
