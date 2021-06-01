package wooteco.subway.path.domain.fare;

import wooteco.subway.path.domain.fare.age.AgeStrategy;
import wooteco.subway.path.domain.fare.distance.DistanceStrategy;

public class Fare {
    public static final int BASIC_FARE = 1250;

    private final DistanceStrategy distanceStrategy;
    private final AgeStrategy ageStrategy;

    public Fare(DistanceStrategy distanceStrategy, AgeStrategy ageStrategy) {
        this.distanceStrategy = distanceStrategy;
        this.ageStrategy = ageStrategy;
    }

    public int calculate(int extraFare, int distance) {
        int fare = BASIC_FARE + extraFare;
        fare += distanceStrategy.calculate(distance);
        return ageStrategy.calculate(fare);
    }
}
