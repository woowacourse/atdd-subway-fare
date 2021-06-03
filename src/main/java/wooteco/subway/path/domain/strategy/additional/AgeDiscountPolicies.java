package wooteco.subway.path.domain.strategy.additional;

import java.util.List;

public class AgeDiscountPolicies {
    private static final List<AgeDiscountPolicy> discountPolicies;
    private static final int DEFAULT_DISCOUNT_FARE = 350;

    static {
        discountPolicies = List.of(new NoDiscount(DEFAULT_DISCOUNT_FARE), new InfancyDiscount(DEFAULT_DISCOUNT_FARE),
                new ChildrenDiscount(DEFAULT_DISCOUNT_FARE), new AdolescenceDiscount(DEFAULT_DISCOUNT_FARE) );
    }

    public static AgeDiscountPolicy instanceOf(int age) {
        return discountPolicies.stream()
                .filter(it -> it.match(age))
                .findFirst()
                .orElse(new NoDiscount(DEFAULT_DISCOUNT_FARE));
    }
}
