package wooteco.subway.path.domain.policy.discountpolicy;

import java.math.BigDecimal;
import java.util.function.UnaryOperator;
import wooteco.subway.member.domain.LoginMember;

public interface DiscountPolicy {

    static UnaryOperator<BigDecimal> zero() {
        return fare -> BigDecimal.ZERO;
    }

    boolean isSatisfied(LoginMember loginMember);

    BigDecimal calculate(LoginMember loginMember, BigDecimal currentFare);
}
