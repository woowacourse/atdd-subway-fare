package wooteco.subway.path.application;

public class PathNotConnectedException extends InvalidPathException {
    public PathNotConnectedException(String errorMessage) {
        super(errorMessage);
    }
}
