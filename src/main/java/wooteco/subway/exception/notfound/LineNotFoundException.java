package wooteco.subway.exception.notfound;

import org.springframework.http.HttpStatus;

public class LineNotFoundException extends NotFoundException {

    private static final String LINE_NOT_FOUND = "해당하는 노선을 찾을 수 없습니다.";

    public LineNotFoundException() {
        super(HttpStatus.BAD_REQUEST, LINE_NOT_FOUND);
    }
}
