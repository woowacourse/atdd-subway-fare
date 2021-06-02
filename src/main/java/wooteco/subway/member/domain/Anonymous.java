package wooteco.subway.member.domain;

import wooteco.subway.path.domain.DiscountPolicy;

public class Anonymous extends RequestUser {
    private static final int ADULT_AGE = 20;

    public Anonymous() {
        super(-1L, "anonymous", ADULT_AGE);
    }

    @Override
    public boolean isAnonymous() {
        return true;
    }
}
