package wooteco.subway.auth.domain;

import wooteco.subway.auth.application.AuthorizationException;

public class AnonymousUser extends User {
    public AnonymousUser() {
        super(null, null, null);
    }

    @Override
    public boolean isLoggedIn() {
        return false;
    }

    @Override
    public Long getId() {
        throw new AuthorizationException();
    }

    @Override
    public String getEmail() {
        throw new AuthorizationException();
    }

    @Override
    public Integer getAge() {
        throw new AuthorizationException();
    }
}
