package wooteco.subway.exception.dto;

import org.springframework.http.HttpStatus;

public class ExceptionResponse {
    public static final ExceptionResponse INTERVAL_SERVER_ERROR =
            new ExceptionResponse("서버에서 요청을 처리하지 못했습니다.", HttpStatus.INTERNAL_SERVER_ERROR.value());

    private String message;
    private int status;

    public ExceptionResponse() {
    }

    public ExceptionResponse(String message, int status) {
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
