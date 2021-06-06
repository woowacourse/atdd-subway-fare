package wooteco.subway.exception.dto;

public class ErrorDto {
    String message;

    public ErrorDto() {
        this("something went wrong!");
    }

    public ErrorDto(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
