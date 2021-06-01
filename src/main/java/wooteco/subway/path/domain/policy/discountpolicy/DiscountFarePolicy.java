package wooteco.subway.path.domain.policy.discountpolicy;

import java.math.BigDecimal;
import java.util.function.UnaryOperator;
import wooteco.subway.member.domain.LoginMember;

public interface DiscountFarePolicy {

    static UnaryOperator<BigDecimal> zero() {
        return fare -> BigDecimal.ZERO;
    }

    UnaryOperator<BigDecimal> calculate(LoginMember loginMember);
}
