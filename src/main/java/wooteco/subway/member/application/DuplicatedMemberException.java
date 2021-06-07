package wooteco.subway.member.application;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicatedMemberException extends RuntimeException {

    public DuplicatedMemberException(String message) {
        super(message);
    }
}
