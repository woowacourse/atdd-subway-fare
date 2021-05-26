package wooteco.subway.path.domain;

import java.util.Arrays;

public enum FareDiscountByAge {
    CHILDREN(6, 13, 0.5),
    TEENAGER(13, 19, 0.2),
    OTHERS(0, 0, 0);

    private static final int deductibleFare = 350;

    private final int ageFrom;
    private final int ageTo;
    private final double discountRate;

    FareDiscountByAge(int ageFrom, int ageTo, double discountRate) {
        this.ageFrom = ageFrom;
        this.ageTo = ageTo;
        this.discountRate = discountRate;
    }

    public static int calculate(int memberAge, int totalFare) {
        final FareDiscountByAge agePolicyType = Arrays.stream(values())
                .filter(agePolicy -> agePolicy.ageFrom <= memberAge && memberAge < agePolicy.ageTo)
                .findAny()
                .orElse(OTHERS);

        return agePolicyType.applyDiscount(totalFare);
    }

    private int applyDiscount(int totalFare) {
        return (int) ((totalFare - deductibleFare) * (1 - this.discountRate)) + deductibleFare;
    }
}
