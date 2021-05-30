package wooteco.subway.exception.delete;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "구간을 제거 할 수 없습니다.")
public class SectionDeleteException extends RuntimeException{
}
