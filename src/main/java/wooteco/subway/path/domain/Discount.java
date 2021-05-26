package wooteco.subway.path.domain;

import wooteco.subway.member.domain.Age;

import java.util.Arrays;
import java.util.function.Predicate;

public enum Discount {
    CHILDREN(0.5, Age::isChildren),
    TEENAGER(0.2, Age::isTeenager),
    NONE(0, (age) -> true);

    public static final int DEFAULT_DISCOUNT_AMOUNT = 350;

    private final double discountRate;
    private final Predicate<Age> discountScheme;

    Discount(final double discountRate, final Predicate<Age> discountScheme) {
        this.discountRate = discountRate;
        this.discountScheme = discountScheme;
    }

    public static int getDiscountAmount(final Age age, final int fareAmount) {
        Double discountRate = Arrays.stream(values())
                .filter(it -> it.discountScheme.test(age))
                .findFirst()
                .map(it -> it.discountRate)
                .orElseThrow(IllegalArgumentException::new);

        return (int) ((fareAmount - DEFAULT_DISCOUNT_AMOUNT) * discountRate);
    }
}
