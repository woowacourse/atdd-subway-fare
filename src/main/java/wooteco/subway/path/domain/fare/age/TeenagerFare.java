package wooteco.subway.path.domain.fare.age;

import wooteco.subway.path.domain.fare.Fare;

public class TeenagerFare implements AgeFare {
    private static final double DISCOUNT_RATE = 0.2;

    @Override
    public Fare getFare(Fare currentFare) {
        Fare discountedFare = currentFare.sub(new Fare(CHILD_OR_TEENAGER_SUBTRACT_FARE));
        return discountedFare.discount(DISCOUNT_RATE);
    }
}
