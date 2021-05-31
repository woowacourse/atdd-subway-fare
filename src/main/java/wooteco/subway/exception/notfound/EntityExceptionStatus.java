package wooteco.subway.exception.notfound;

import wooteco.subway.exception.ExceptionStatus;

public enum EntityExceptionStatus implements ExceptionStatus {
    LINE_NOT_FOUND(404, "존재하지 않는 노선입니다."),
    STATION_NOT_FOUND(404, "존재하지 않는 역입니다.");

    private final int status;
    private final String message;

    EntityExceptionStatus(int status, String message) {
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
