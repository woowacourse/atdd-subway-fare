package wooteco.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "지원되지 않는 언어, 공백, 특수문자는 입력 불가능합니다.")
public class UnsupportedSubwayCharacterException extends RuntimeException {
}
