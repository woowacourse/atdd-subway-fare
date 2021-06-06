package wooteco.subway.path.domain.policy.discountpolicy;

import java.math.BigDecimal;
import wooteco.subway.exception.SubwayException;
import wooteco.subway.member.domain.LoginMember;

public abstract class AgeDiscountFarePolicy implements DiscountPolicy {

    @Override
    public boolean isSatisfied(LoginMember loginMember) {
        return loginMember.isPresent() &&
            minAge() <= loginMember.getAge() &&
            loginMember.getAge() < maxAge();
    }

    @Override
    public BigDecimal calculate(LoginMember loginMember, BigDecimal currentFare) {
        if (!isSatisfied(loginMember)) {
            throw new SubwayException("적용할 수 없는 나이 입니다.");
        }

        return calculateDiscountFare(currentFare);
    }

    protected abstract int minAge();

    protected abstract int maxAge();

    protected abstract BigDecimal calculateDiscountFare(BigDecimal currentFare);
}
