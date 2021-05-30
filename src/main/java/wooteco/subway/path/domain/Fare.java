package wooteco.subway.path.domain;

import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.age.FareByAge;

public class Fare {
    private int fare;
    private LoginMember loginMember;

    public Fare() {
    }

    public Fare(int fareByDistanceAndLine, LoginMember loginMember) {
        this.fare = fareByDistanceAndLine;
        this.loginMember = loginMember;
    }

    public int getFare() {
        return FareByAge.calculate(fare, loginMember);
    }
}
