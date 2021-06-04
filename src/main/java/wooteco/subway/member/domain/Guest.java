package wooteco.subway.member.domain;

import wooteco.subway.exception.AuthorizationException;

public class Guest implements User {

    @Override
    public Long getId() {
        throw new AuthorizationException();
    }

    @Override
    public String getEmail() {
        throw new AuthorizationException();
    }

    @Override
    public String getPassword() {
        throw new AuthorizationException();
    }

    @Override
    public int getAge() {
        throw new AuthorizationException();
    }
}
