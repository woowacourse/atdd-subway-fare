package wooteco.auth.domain;

public enum Role {
    ANONYMOUS, USER;

    public boolean isAnonymous() {
        return this.equals(ANONYMOUS);
    }
}
