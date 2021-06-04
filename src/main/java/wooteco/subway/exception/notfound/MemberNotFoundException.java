package wooteco.subway.exception.notfound;

import org.springframework.http.HttpStatus;

public class MemberNotFoundException extends NotFoundException {

    private final static String MEMBER_NOT_FOUND_MESSAGE = "존재하지 않는 유저입니다.";

    public MemberNotFoundException() {
        super(HttpStatus.BAD_REQUEST, MEMBER_NOT_FOUND_MESSAGE);
    }
}
