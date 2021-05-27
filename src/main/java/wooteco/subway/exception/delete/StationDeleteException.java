package wooteco.subway.exception.delete;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "구간에 존재하는 지하 역을 삭제할 수 없습니다.")
public class StationDeleteException extends RuntimeException{
}
