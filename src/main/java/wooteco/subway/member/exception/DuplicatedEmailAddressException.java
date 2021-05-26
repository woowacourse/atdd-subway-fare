package wooteco.subway.member.exception;

public class DuplicatedEmailAddressException extends RuntimeException {

    public DuplicatedEmailAddressException(String email) {
        super(String.format("이미 존재하는 email 입니다.(%s)", email));
    }
}
