package wooteco.subway;

public class SubwayException extends RuntimeException {
    private final String code;

    public SubwayException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
