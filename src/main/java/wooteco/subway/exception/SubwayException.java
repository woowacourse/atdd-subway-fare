package wooteco.subway.exception;

public class SubwayException extends RuntimeException {

    private final SubwayExceptionSetInterface subWayExceptionSetInterface;

    public SubwayException(SubwayExceptionSetInterface subWayExceptionSetInterface) {
        super(subWayExceptionSetInterface.message());
        this.subWayExceptionSetInterface = subWayExceptionSetInterface;
    }

    public String message() {
        return subWayExceptionSetInterface.message();
    }

    public int status() {
        return subWayExceptionSetInterface.status();
    }
}
