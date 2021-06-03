package wooteco.subway.member.domain;

import wooteco.subway.fare.domain.AgeDiscountStrategy;

public class AnonymousUser implements User {

    @Override
    public int getAge() {
        return AgeDiscountStrategy.ADULT_AGE;
    }
}
