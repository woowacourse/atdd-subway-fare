package wooteco.auth.domain;

public enum Role {
    ANONYMOUS,
    USER;

    public boolean isAnonymous() {
        return this.equals(ANONYMOUS);
    }

    public boolean isUser() {
        return this.equals(USER);
    }
}
