package wooteco.subway.path.domain;

import java.util.Arrays;
import java.util.function.IntPredicate;

public enum Discount {
    CHILDREN(0.5, (age) -> 6 <= age && age < 13),
    TEENAGER(0.2, (age) -> 13 <= age && age < 19),
    NORMAL(0, (age) -> true);

    public static final int DEFAULT_DISCOUNT_AMOUNT = 350;

    private double discountRate;
    private IntPredicate discountCriteria;

    Discount(final double discountRate, final IntPredicate discountCriteria) {
        this.discountRate = discountRate;
        this.discountCriteria = discountCriteria;
    }

    public static int getDiscountAmount(final int age, final int fareAmount) {
        Double discountRate = Arrays.stream(values())
                .filter(it -> it.discountCriteria.test(age))
                .findFirst()
                .map(it -> it.discountRate)
                .orElseThrow(IllegalArgumentException::new);

        return (int) ((fareAmount - DEFAULT_DISCOUNT_AMOUNT) * discountRate);
    }
}
