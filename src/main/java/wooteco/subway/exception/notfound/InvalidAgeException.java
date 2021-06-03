package wooteco.subway.exception.notfound;

public class InvalidAgeException extends NotFoundException {
    public InvalidAgeException() {
        super("나이 입력이 잘못 되어 요금 정책을 적용할 수 없습니다.");
    }
}
