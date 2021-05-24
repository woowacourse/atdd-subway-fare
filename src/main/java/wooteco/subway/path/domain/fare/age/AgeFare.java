package wooteco.subway.path.domain.fare.age;

import wooteco.subway.path.domain.fare.Fare;

public interface AgeFare {
    int BABY_END_AGE = 5;
    int CHILD_END_AGE = 12;
    int TEENAGER_END_AGE = 18;
    int CHILD_OR_TEENAGER_SUBTRACT_FARE = 350;

    static AgeFare of(int age) {
        if (age <= BABY_END_AGE) {
            return new BabyFare();
        }
        if (age <= CHILD_END_AGE) {
            return new ChildFare();
        }
        if (age <= TEENAGER_END_AGE) {
            return new TeenagerFare();
        }
        return new AdultFare();
    }

    Fare getFare(Fare currentFare);
}
