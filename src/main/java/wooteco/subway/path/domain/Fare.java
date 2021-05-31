package wooteco.subway.path.domain;

import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.policy.FareByAge;

public class Fare {
    private int fare;

    public Fare() {
    }

    public Fare(int fare) {
        this.fare = fare;
    }

    public void calculateFareByAge(LoginMember loginMember) {
        this.fare = FareByAge.calculate(fare, loginMember);
    }

    public int getFare() {
        return fare;
    }
}
