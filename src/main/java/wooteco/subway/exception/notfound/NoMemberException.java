package wooteco.subway.exception.notfound;

public class NoMemberException extends NotFoundException {
    public NoMemberException() {
        super("이메일과 비밀번호를 확인해주세요.");
    }
}
