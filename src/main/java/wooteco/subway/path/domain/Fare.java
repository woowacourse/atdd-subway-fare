package wooteco.subway.path.domain;

import wooteco.subway.path.domain.strategy.DistanceFarePolicy;

public class Fare {
    private final DistanceFarePolicy distanceFarePolicy;
    private final int extraFare;

    public Fare(DistanceFarePolicy distanceFarePolicy, int extraFare) {
        this.distanceFarePolicy = distanceFarePolicy;
        this.extraFare = extraFare;
    }

    public int calculateFare() {
        return distanceFarePolicy.calculateFare(extraFare);
    }
}
