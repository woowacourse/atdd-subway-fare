package wooteco.subway.member.domain;

import wooteco.subway.path.domain.DiscountPolicy;

public class Anonymous extends RequestUser {
    private static final Long DUMMY_ID = -1L;
    private static final String DUMMY_EMAIL = "anonymous";
    private static final int ADULT_AGE = 20;

    public Anonymous() {
        super(DUMMY_ID, DUMMY_EMAIL, ADULT_AGE);
    }

    @Override
    public boolean isAnonymous() {
        return true;
    }
}
