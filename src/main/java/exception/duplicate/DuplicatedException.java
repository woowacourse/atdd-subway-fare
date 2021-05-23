package exception.duplicate;

import exception.SubwayException;

public class DuplicatedException extends SubwayException {
    public DuplicatedException(String message) {
        super(message);
    }
}
