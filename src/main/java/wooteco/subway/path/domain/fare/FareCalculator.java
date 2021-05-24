package wooteco.subway.path.domain.fare;

import java.util.Comparator;
import java.util.List;
import wooteco.subway.line.domain.Line;

public class FareCalculator {
    private static final int AGE_DISCOUNT_SUBTRACT_FARE = 350;
    private static final double CHILD_DISCOUNT_RATE = 0.5;
    private static final double TEENAGER_DISCOUNT_RATE = 0.2;
    private static final int CHILD_START_AGE = 6;
    private static final int TEENAGER_START_AGE = 13;
    private static final int ADULT_START_AGE = 19;

    public Fare getFareWithLineExtraFare(Fare currentFare, List<Line> lines) {
        return lines.stream()
            .map(Line::getExtraFare)
            .max(Comparator.comparingInt(Fare::getFare))
            .orElseThrow(() -> new IllegalArgumentException("Line이 한 개 이상 존재해야 합니다."))
            .add(currentFare);
    }

    public Fare getFareByAge(int age, Fare currentFare) {
        if (age < CHILD_START_AGE) {
            return new Fare(0);
        }
        if (age < TEENAGER_START_AGE) {
            return getFareDiscountedByChildAge(currentFare);
        }
        if (age < ADULT_START_AGE) {
            return getFareDiscountedByTeenagerAge(currentFare);
        }
        return currentFare;
    }

    private Fare getFareDiscountedByChildAge(Fare currentFare) {
        Fare discountedFare = currentFare.sub(new Fare(AGE_DISCOUNT_SUBTRACT_FARE));
        return discountedFare.discount(CHILD_DISCOUNT_RATE);
    }

    private Fare getFareDiscountedByTeenagerAge(Fare currentFare) {
        Fare discountedFare = currentFare.sub(new Fare(AGE_DISCOUNT_SUBTRACT_FARE));
        return discountedFare.discount(TEENAGER_DISCOUNT_RATE);
    }
}
