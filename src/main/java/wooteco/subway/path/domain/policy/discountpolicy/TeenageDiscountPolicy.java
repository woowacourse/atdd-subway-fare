package wooteco.subway.path.domain.policy.discountpolicy;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.UnaryOperator;
import wooteco.subway.member.domain.LoginMember;

public class TeenageDiscountPolicy implements DiscountFarePolicy {
    private static final int DEDUCTION = 350;
    private static final int MINIMUM_AGE = 13;
    private static final int MAXIMUM_AGE = 19;
    private static final double DISCOUNTED_RATE = 0.2;

    @Override
    public UnaryOperator<BigDecimal> calculate(Integer age) {
        if (isDiscountable(age)) {
            return fare -> fare.subtract(BigDecimal.valueOf(DEDUCTION))
                .multiply(BigDecimal.valueOf(DISCOUNTED_RATE));
        }

        return DiscountFarePolicy.zero();
    }

    private boolean isDiscountable(Integer age) {
        Objects.requireNonNull(age);
        return MINIMUM_AGE <= age && age < MAXIMUM_AGE;
    }

}
