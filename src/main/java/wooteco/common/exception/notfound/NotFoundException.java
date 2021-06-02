package wooteco.common.exception.notfound;

public class NotFoundException extends RuntimeException {

    public NotFoundException(NotFoundMessage notFoundMessage) {
        super(notFoundMessage.message);
    }

    public enum NotFoundMessage {
        MEMBER("존재하지 않는 유저입니다.");

        private final String message;

        NotFoundMessage(String message) {
            this.message = message;
        }
    }
}
