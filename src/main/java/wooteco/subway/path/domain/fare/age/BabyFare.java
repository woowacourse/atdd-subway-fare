package wooteco.subway.path.domain.fare.age;

import wooteco.subway.path.domain.fare.Fare;

public class BabyFare implements AgeFare {
    @Override
    public Fare getFare(Fare currentFare) {
        return new Fare(0);
    }
}
