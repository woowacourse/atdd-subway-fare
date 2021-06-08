package wooteco.subway.path.application;

import lombok.Getter;
import wooteco.subway.path.domain.Fare;

import java.util.Arrays;

@Getter
public enum AgeDiscountPolicy {
    INFANT(6, 1.0, new Fare(0)),
    CHILD(12, 0.5, new Fare(350)),
    TEENAGER(18, 0.2, new Fare(350)),
    ADULT(Integer.MAX_VALUE, 0.0, new Fare(0));

    private final int age;
    private final double discountRatio;
    private final Fare preDiscountFare;

    AgeDiscountPolicy(int age, double discountRatio, Fare preDiscountFare) {
        this.age = age;
        this.discountRatio = discountRatio;
        this.preDiscountFare = preDiscountFare;
    }

    public static Fare discountFareByAge(int age, Fare fare) {
        return Arrays.stream(values())
                .filter(it -> it.getAge() >= age)
                .findFirst()
                .orElse(ADULT)
                .applyDiscount(fare);
    }

    private Fare applyDiscount(Fare fare) {
        return fare.sub(this.preDiscountFare).discount(this.discountRatio);
    }
}
