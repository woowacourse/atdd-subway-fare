package wooteco.subway.member.domain;

import wooteco.subway.exception.AuthorizationException;

public class Guest implements User{
    @Override
    public Long getId() {
        throw new AuthorizationException("로그인 후 시도하세요.");
    }

    @Override
    public String getEmail() {
        throw new AuthorizationException("로그인 후 시도하세요.");
    }

    @Override
    public String getPassword() {
        throw new AuthorizationException("로그인 후 시도하세요.");
    }

    @Override
    public int getAge() {
        throw new AuthorizationException("로그인 후 시도하세요.");
    }

    @Override
    public boolean isGuest() {
        return true;
    }
}
