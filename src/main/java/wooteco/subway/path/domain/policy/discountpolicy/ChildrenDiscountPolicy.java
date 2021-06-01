package wooteco.subway.path.domain.policy.discountpolicy;

import java.math.BigDecimal;
import java.util.function.UnaryOperator;
import wooteco.subway.member.domain.LoginMember;

public class ChildrenDiscountPolicy implements DiscountFarePolicy {

    private static final int DEDUCTION = 350;
    private static final int MINIMUM_AGE = 6;
    private static final int MAXIMUM_AGE = 13;
    private static final double DISCOUNTED_RATE = 0.5;

    @Override
    public UnaryOperator<BigDecimal> calculate(LoginMember loginMember) {
        if (isDiscountable(loginMember)) {
            return fare -> fare.subtract(BigDecimal.valueOf(DEDUCTION))
                .multiply(BigDecimal.valueOf(DISCOUNTED_RATE));
        }

        return DiscountFarePolicy.zero();
    }

    private boolean isDiscountable(LoginMember loginMember) {
        return loginMember.isPresent() &&
            MINIMUM_AGE <= loginMember.getAge() &&
            loginMember.getAge() < MAXIMUM_AGE;
    }

}
