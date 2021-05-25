package wooteco.subway.exception;

public class ErrorDto {
    String message;

    public ErrorDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
