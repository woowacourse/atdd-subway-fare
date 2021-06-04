package wooteco.subway.line.domain.fare;

import wooteco.subway.line.domain.fare.policy.FarePolicy;

public class Fare {
    private final FarePolicy farePolicy;

    public Fare(FarePolicy farePolicy) {
        this.farePolicy = farePolicy;
    }

    public int calculateTotalFare(int distance, int maxExtraLineFare) {
        int fare = DistancePolicy.distanceFare(distance);
        return this.farePolicy.discount(fare + maxExtraLineFare);
    }
}
