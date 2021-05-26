package wooteco.subway.member.exception;

public class DuplicatedEmailAddressException extends RuntimeException {

    public DuplicatedEmailAddressException(String email) {
        super(String.format("해당 email은 중복되었습니다.(%s)", email));
    }
}
