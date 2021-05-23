package wooteco.subway.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // COMMON
    INVALID_INPUT("[ERROR] 유효하지 않는 요청 값입니다.", HttpStatus.BAD_REQUEST.value()),

    // AUTH
    INVALID_LOGIN("[ERROR] 이메일 혹은 비밀번호가 틀렸습니다. 다시 입력해주세요.", HttpStatus.UNAUTHORIZED.value()),
    INVALID_TOKEN("[ERROR] 유효하지 않는 토큰 정보입니다.", HttpStatus.UNAUTHORIZED.value()),

    // MEMBER
    DUPLICATE_EMAIL("[ERROR] 이미 존재하는 이메일입니다. 다른 이메일을 사용해주세요.", HttpStatus.BAD_REQUEST.value()),

    // STATION
    DUPLICATE_STATION_NAME("[ERROR] 이미 존재하는 지하철 역 이름입니다. 다른 이름을 사용해주세요.", HttpStatus.BAD_REQUEST.value()),
    INVALID_STATION_DELETION("[ERROR] 노선에 포함된 지하철 역이므로 삭제할 수 없습니다.", HttpStatus.BAD_REQUEST.value());

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
