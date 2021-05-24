package wooteco.subway.exception.badrequest;

public class StationDuplicateNameException extends BadRequestException {
    public StationDuplicateNameException(String name) {
        super(name + "은 이미 존재하는 역 이름입니다.");
    }
}