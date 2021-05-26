package wooteco.subway.exception;

public class SubwayCustomException extends RuntimeException {

    private final SubwayException subWayException;

    public SubwayCustomException(SubwayException subWayException) {
        super(subWayException.message());
        this.subWayException = subWayException;
    }

    public String message() {
        return subWayException.message();
    }

    public int status() {
        return subWayException.status();
    }
}
