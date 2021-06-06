package wooteco.common.exception.badrequest;

public class MemberNotFoundException extends BadRequestException {

    public MemberNotFoundException() {
        super("존재하지 않는 회원입니다.");
    }
}
