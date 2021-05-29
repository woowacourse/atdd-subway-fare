package wooteco.subway.path.domain;

import wooteco.subway.member.domain.Age;

import java.util.Arrays;
import java.util.function.Predicate;

public enum Discount {
    CHILDREN(0.5, Age::isChildren),
    TEENAGER(0.2, Age::isTeenager),
    NONE(0, (age) -> !age.isChildren() && !age.isTeenager());

    private static final int DEFAULT_DISCOUNT_AMOUNT = 350;

    private final double discountRate;
    private final Predicate<Age> discountScheme;

    Discount(final double discountRate, final Predicate<Age> discountScheme) {
        this.discountRate = discountRate;
        this.discountScheme = discountScheme;
    }

    public static Discount getDiscount(final Age age) {
        return Arrays.stream(values())
                .filter(it -> it.discountScheme.test(age))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public int calculateDiscountAmount(final int fare) {
        return (int) ((fare - DEFAULT_DISCOUNT_AMOUNT) * discountRate);
    }
}
