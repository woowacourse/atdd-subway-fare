package wooteco.subway.path.domain;

import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.age.FareByAge;

public class Fare {
    private int fare;

    public Fare() {
    }

    public Fare(int fare) {
        this.fare = fare;
    }

    public Fare(int fareByDistanceAndLine, LoginMember loginMember) {
        this.fare = FareByAge.calculate(fareByDistanceAndLine, loginMember);
    }

    public int getFare() {
        return fare;
    }
}
