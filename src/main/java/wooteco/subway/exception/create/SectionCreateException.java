package wooteco.subway.exception.create;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "구간 입력이 잘못 되었습니다.")
public class SectionCreateException extends RuntimeException {

}
