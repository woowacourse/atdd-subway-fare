package wooteco.subway.member.domain;

import wooteco.subway.exception.SubwayException;

public class IllegalAgeException extends SubwayException {
    private static final String MESSAGE = "나이는 1이상 200 이하만 가능합니다.";

    public IllegalAgeException() {
        super(MESSAGE);
    }
}
