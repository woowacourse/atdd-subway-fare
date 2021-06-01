package wooteco.subway.path.domain.fare;

import wooteco.subway.path.domain.fare.age.AgeStrategy;
import wooteco.subway.path.domain.fare.distance.ChainOfDistance;

public class Fare {
    private final AgeStrategy ageStrategy;

    public Fare(AgeStrategy ageStrategy) {
        this.ageStrategy = ageStrategy;
    }

    public int calculate(int extraFare, int distance) {
        ChainOfDistance chainOfDistance = new ChainOfDistance();
        int fare = chainOfDistance.calculate(distance) + extraFare;
        return ageStrategy.calculate(fare);
    }
}
