package wooteco.subway.exception;

public enum SubwayExceptions {
    DUPLICATED_ID("중복되는 이메일이 존재합니다."),
    DUPLICATED_LINE_NAME("중복되는 노선이 존재합니다."),
    DUPLICATED_STATION_NAME("중복되는 역 이름이 존재합니다."),
    BOTH_STATION_ALREADY_REGISTERED_IN_LINE("이미 두 역이 노선에 존재합니다."),
    BOTH_STATION_NOT_REGISTERED_IN_LINE("두 역이 노선에 이미 존재하지 않습니다."),
    ONLY_ONE_SECTION_EXIST("오직 하나의 구간이 남아 삭제할 수 없습니다."),
    SAME_STATIONS_IN_SAME_SECTION("같은 역을 한 구간에 등록할 수 없습니다."),
    STATION_ALREADY_REGISTERED_IN_LINE("노선에 등록된 역은 삭제할 수 없습니다."),
    IMPOSSIBLE_DISTANCE("추가하고자 하는 구간의 거리가 기존 구간의 거리보다 깁니다."),
    INVALID_AGE("나이가 유효하지 않습니다."),
    INVALID_DISTANCE("거리가 유효하지 않습니다."),
    INVALID_EMAIL("이메일이 유효하지 않습니다."),
    INVALID_INPUT("입력이 올바르지 않습니다."),
    INVALID_NAME("이름 입력이 올바르지 않습니다."),
    INVALID_PASSWORD("비밀번호가 유효하지 않습니다."),
    INVALID_TOKEN("토큰이 유효하지 않습니다."),
    INVALID_PATH("경로가 유효하지 않습니다."),
    MISMATCH_ID_PASSWORD("아이디와 비밀번호가 일치하지 않습니다."),
    NO_SUCH_LINE("해당 노선이 존재하지 않습니다."),
    NO_SUCH_SECTION("구간이 존재하지 않습니다."),
    NO_SUCH_STATION("해당 역이 존재하지 않습니다.");

    private final SubwayException subwayException;

    SubwayExceptions(String message) {
        this.subwayException = new SubwayException(name(), message);
    }

    public SubwayException makeException() {
        return subwayException;
    }
}
