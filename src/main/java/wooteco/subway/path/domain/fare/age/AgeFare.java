package wooteco.subway.path.domain.fare.age;

import wooteco.subway.path.domain.fare.Fare;

public interface AgeFare {
    int CHILD_OR_TEENAGER_SUBTRACT_FARE = 350;

    Fare getFare(Fare currentFare);
}
