package wooteco.subway.exception.value;

import wooteco.subway.exception.ExceptionStatus;

public enum InvalidValueExceptionStatus implements ExceptionStatus {
    INVALID_PATH(400, "경로를 조회할 수 없습니다."),
    INVALID_DISTANCE(400, "잘못된 노선 구간 거리입니다."),
    INVALID_FARE(400, "요금 계산시 사용할 거리 혹은 나이 값이 잘못되었습니다."),
    DUPLICATED_EMAIL(400, "중복된 이메일입니다."),
    DUPLICATED_LINE_COLOR(400, "잘못된 노선 색상입니다."),
    DUPLICATED_LINE_NAME(400, "존재하는 노선 이름입니다."),
    DUPLICATED_STATION_NAME(400, "존재하는 역 이름입니다."),
    SECTION_NOT_DELETABLE(400, "노선에는 최소한 하나의 구간은 존재해야합니다."),
    SECTION_NOT_ADDABLE(400, "구간 추가에 필요한 정보가 잘못되었습니다."),
    STATION_NOT_DELETABLE(400, "이미 노선에 등록된 역은 삭제할 수 없습니다.");

    private final int status;
    private final String message;

    InvalidValueExceptionStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
