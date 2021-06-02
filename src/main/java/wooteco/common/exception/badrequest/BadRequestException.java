package wooteco.common.exception.badrequest;

public class BadRequestException extends RuntimeException {

    public BadRequestException(BadRequestMessage badRequestMessage) {
        super(badRequestMessage.message);
    }

    public enum BadRequestMessage {
        INVALID_PATH("잘못된 구간 정보입니다."),
        LINE_COLOR_DUPLICATE("이미 존재하는 노선 색이 있습니다."),
        LINE_NAME_DUPLICATE("이미 존재하는 노선 이름이 있습니다."),
        NOT_REMOVAL_STATION("구간에 등록되어있어 삭제할 수 없는 역입니다."),
        STATION_NAME_DUPLICATE("이미 존재하는 역 이름입니다.");

        private final String message;

        BadRequestMessage(String message) {
            this.message = message;
        }
    }
}
