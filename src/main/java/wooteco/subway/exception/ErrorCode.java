package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // COMMON
    INVALID_INPUT("[ERROR] 유효하지 않는 요청 값입니다.", HttpStatus.BAD_REQUEST.value()),

    // AUTH
    INVALID_LOGIN("이메일 혹은 비밀번호를 다시 확인해주세요", HttpStatus.BAD_REQUEST.value()),
    INVALID_TOKEN("다시 로그인 후 시도해주세요", HttpStatus.UNAUTHORIZED.value()),

    // MEMBER
    DUPLICATE_EMAIL("이미 가입된 이메일입니다", HttpStatus.BAD_REQUEST.value()),
    NOTFOUND_MEMBER("존재하지 않는 회원입니다", HttpStatus.NOT_FOUND.value()),
    SAME_PASSWORD("현재 사용 중인 비밀번호입니다. 다른 비밀번호를 입력해주세요", HttpStatus.BAD_REQUEST.value()),
    INVALID_PASSWORD("현재 비밀번호를 다시 확인해주세요", HttpStatus.BAD_REQUEST.value()),

    // STATION
    DUPLICATE_STATION_NAME("이미 존재하는 지하철 역입니다", HttpStatus.BAD_REQUEST.value()),
    INVALID_STATION_DELETION("노선에 포함된 지하철 역이므로 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST.value()),
    NOTFOUND_STATION("존재하지 않는 지하철역입니다", HttpStatus.NOT_FOUND.value()),

    // Line
    DUPLICATE_LINE_NAME("지하철 노선 이름이 이미 존재합니다", HttpStatus.BAD_REQUEST.value()),
    NOTFOUND_LINE("존재하지 않는 지하철노선입니다", HttpStatus.NOT_FOUND.value()),
    DUPLICATE_LINE_COLOR("이미 존재하는 노선 색입니다.", HttpStatus.BAD_REQUEST.value()),

    // Section
    INVALID_SECTION("유효하지 않는 구간 요청입니다", HttpStatus.BAD_REQUEST.value());

    private final String message;
    private final int status;

    ErrorCode(String message, int status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }
}
