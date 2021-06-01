package wooteco.subway.path.domain.policy.discountpolicy;

import java.math.BigDecimal;

public class ChildrenAgeFarePolicy extends AgeDiscountFarePolicy {

    private static final int DEDUCTION = 350;
    private static final int MINIMUM_AGE = 6;
    private static final int MAXIMUM_AGE = 13;
    private static final double DISCOUNTED_RATE = 0.5;


    @Override
    protected int minAge() {
        return MINIMUM_AGE;
    }

    @Override
    protected int maxAge() {
        return MAXIMUM_AGE;
    }

    @Override
    protected BigDecimal calculateDiscountFare(BigDecimal currentFare) {
        return currentFare.subtract(BigDecimal.valueOf(DEDUCTION))
            .multiply(BigDecimal.valueOf(DISCOUNTED_RATE));
    }
}
