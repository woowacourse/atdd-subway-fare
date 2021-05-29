package wooteco.subway.path.domain.fare;

import wooteco.subway.path.domain.fare.age.AgeStrategy;
import wooteco.subway.path.domain.fare.distance.DistanceStrategy;

public class Fare {
    private static final int BASIC_FARE = 1250;
    private final DistanceStrategy distanceStrategy;
    private final AgeStrategy ageStrategy;
    private int fare;

    public Fare(int extraFare, DistanceStrategy distanceStrategy, AgeStrategy ageStrategy) {
        this.fare = BASIC_FARE + extraFare;
        this.distanceStrategy = distanceStrategy;
        this.ageStrategy = ageStrategy;
    }

    public int calculate(int distance) {
        fare += distanceStrategy.calculate(distance);
        return ageStrategy.calculate(fare);
    }
}
