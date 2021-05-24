package wooteco.subway.exception;

public class DuplicateNameException extends BadRequestException{
    private final static String DUPLICATE_NAME_ERROR_MESSAGE = "이미 존재하는 역 이름입니다.";
    public DuplicateNameException() {
        super(DUPLICATE_NAME_ERROR_MESSAGE);
    }
}
