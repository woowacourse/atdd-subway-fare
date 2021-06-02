package wooteco.subway.fare.domain.userstrategy;

import wooteco.subway.fare.domain.farestrategy.AgeFare;
import wooteco.subway.fare.domain.farestrategy.DistanceFare;
import wooteco.subway.fare.domain.farestrategy.FarePolicy;
import wooteco.subway.fare.domain.farestrategy.LineFare;
import wooteco.subway.member.domain.LoginMember;

import java.util.Objects;

public class LoginStrategy implements UserStrategy {

    private final int lineExtraFare;
    private final int distance;
    private final LoginMember loginMember;

    public LoginStrategy(int lineExtraFare, int distance, LoginMember loginMember) {
        this.lineExtraFare = lineExtraFare;
        this.distance = distance;
        this.loginMember = loginMember;
    }

    @Override
    public boolean isState() {
        return Objects.nonNull(loginMember);
    }

    @Override
    public FarePolicy generateFarePolicy() {
        return new FarePolicy.Builder()
                .linePolicy(new LineFare(lineExtraFare))
                .distancePolicy(new DistanceFare(distance))
                .agePolicy(new AgeFare(loginMember.getAge()))
                .build();
    }
}
