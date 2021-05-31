package wooteco.subway.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "영어와 숫자만 입력 가능합니다.")
public class UnsupportedCharacterException extends RuntimeException {
}
