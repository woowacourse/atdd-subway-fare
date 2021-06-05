package wooteco.subway.member.domain;

import wooteco.subway.auth.exception.AuthorizationException;
import wooteco.subway.fare.domain.AgeFarePolicy;

public final class Guest implements User {
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

    @Override
    public boolean isOverThan(int age) {
        throw new AuthorizationException();
    }

    @Override
    public AgeFarePolicy farePolicy() {
        return AgeFarePolicy.ADULT;
    }
}
