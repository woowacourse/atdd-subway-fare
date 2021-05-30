package wooteco.subway.path.domain;

import wooteco.subway.path.domain.strategy.DistanceAdditionPolicy;

public class Fare {
    private final DistanceAdditionPolicy distanceAdditionPolicy;
    private final int extraFare;

    public Fare(DistanceAdditionPolicy distanceAdditionPolicy, int extraFare) {
        this.distanceAdditionPolicy = distanceAdditionPolicy;
        this.extraFare = extraFare;
    }

    public int calculateFare() {
        return distanceAdditionPolicy.calculateFare(extraFare);
    }
}
