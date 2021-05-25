package wooteco.common.exception.notfound;

public class MemberNotFoundException extends NotFoundException {

    private static final String MESSAGE = "존재하지 않는 유저입니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }
}
