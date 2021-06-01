package wooteco.subway.aop.exception;

public class ExceptionDto {
    private String code;
    private String errorMessage;

    public ExceptionDto() {
    }

    public ExceptionDto(String code, String errorMessage) {
        this.code = code;
        this.errorMessage = errorMessage;
    }

    public String getCode() {
        return code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
