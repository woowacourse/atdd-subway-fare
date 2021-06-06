package wooteco.subway.path.domain.discount;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;
import wooteco.subway.path.domain.Fare;

public enum AgeDiscountRule {
    ADULT(
            age -> age >= 20,
            fare -> fare
    ),
    TEENAGER(
            age -> 20 > age && age >= 13,
            fare -> fare.subtract(Fare.DEDUCTION).multiply(0.8).add(Fare.DEDUCTION)
    ),
    CHILD(
            age -> 13 > age && age >= 6,
            fare -> fare.subtract(Fare.DEDUCTION).multiply(0.5).add(Fare.DEDUCTION)
    ),
    BABY(
            age -> 6 > age,
            fare -> Fare.ZERO
    );

    private final Predicate<Integer> canDiscount;
    private final Function<Fare, Fare> ageDiscountStrategy;

    AgeDiscountRule(Predicate<Integer> canDiscount, Function<Fare, Fare> function) {
        this.canDiscount = canDiscount;
        this.ageDiscountStrategy = function;
    }

    public static Fare discountByAge(int age, Fare fare) {
        return Arrays.stream(values())
                .filter(ageDiscountRule -> ageDiscountRule.canDiscount(age))
                .findAny()
                .orElseThrow(IllegalArgumentException::new)
                .ageDiscountStrategy.apply(fare);
    }

    public boolean canDiscount(int age) {
        return this.canDiscount.test(age);
    }
}
