package wooteco.subway.path.domain.fare.age;

import wooteco.subway.path.domain.fare.Fare;

public class AdultFare implements AgeFare {
    @Override
    public Fare getFare(Fare currentFare) {
        return currentFare;
    }
}
