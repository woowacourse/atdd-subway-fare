package wooteco.subway.member.exception;

public class EmailAddressNotFoundException extends RuntimeException {

    public EmailAddressNotFoundException(String email) {
        super(String.format("해당 email은 존재하지 않습니다.(%s)", email));
    }
}
