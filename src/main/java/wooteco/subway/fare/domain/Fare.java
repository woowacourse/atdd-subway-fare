package wooteco.subway.fare.domain;

import wooteco.subway.fare.domain.distance.DistanceFarePolicy;

public class Fare {
    private final AgeFarePolicy ageFarePolicy;

    public Fare(AgeFarePolicy ageFarePolicy) {
        this.ageFarePolicy = ageFarePolicy;
    }

    public int calculateTotalFare(int distance, int maxExtraLineFare) {
        int fare = DistanceFarePolicy.calculate(distance);
        return this.ageFarePolicy.discount(fare + maxExtraLineFare);
    }
}
