package wooteco.subway.util;

import java.util.Arrays;

public enum AgePolicy {
    CHILD_POLICY(6, 12, FarePolicy.MINOR_DISCOUNT_FARE, 0.5),
    TEENAGE_POLICY(13, 18, FarePolicy.MINOR_DISCOUNT_FARE, 0.8),
    DEFAULT_POLICY(14, 200, FarePolicy.DEFAULT_DISCOUNT_FARE, 1.0);

    private final int minAge;
    private final int maxAge;
    private final FarePolicy discountFare;
    private final double discountPercentage;

    AgePolicy(final int minAge, final int maxAge, final FarePolicy discountFare, final double discountPercentage) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountFare = discountFare;
        this.discountPercentage = discountPercentage;
    }

    public static int getFareApplyPolicy(final int fare, final int age) {
        AgePolicy policy = Arrays.stream(values())
                .filter(agePolicy -> agePolicy.minAge <= age)
                .filter(agePolicy -> agePolicy.maxAge >= age)
                .findAny().orElse(DEFAULT_POLICY);
        return (int)((fare - policy.discountFare.getFare()) * policy.discountPercentage);
    }
}
