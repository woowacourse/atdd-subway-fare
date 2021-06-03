package wooteco.subway.path.domain.fare;

import wooteco.subway.member.domain.LoginMember;

public class Fare {

    private final int value;

    public Fare(int basicFare) {
        this.value = basicFare;
    }

    public int value() {
        return this.value;
    }
}
