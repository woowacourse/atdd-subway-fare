package wooteco.subway.path.domain.fare;

import wooteco.subway.path.domain.fare.age.AgeStrategy;
import wooteco.subway.path.domain.fare.distance.DistanceChain;

public class Fare {
    private final DistanceChain defaultChain;
    private final AgeStrategy ageStrategy;

    public Fare(DistanceChain defaultChain, AgeStrategy ageStrategy) {
        this.defaultChain = defaultChain;
        this.ageStrategy = ageStrategy;
    }

    public int calculate(int extraFare, int distance) {
        int fare = defaultChain.calculate(distance) + extraFare;
        return ageStrategy.calculate(fare);
    }
}
