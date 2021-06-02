package wooteco.subway.fare.domain.userstrategy;

import wooteco.subway.exception.InternalLogicException;
import wooteco.subway.fare.domain.farestrategy.FarePolicy;
import wooteco.subway.member.domain.LoginMember;

import java.util.Arrays;
import java.util.List;

public class UserStrategyImpl {
    private final List<UserStrategy> userStrategies;

    public UserStrategyImpl(int lineExtraFare, int distance, LoginMember loginMember) {
        this.userStrategies = Arrays.asList(new LoginStrategy(lineExtraFare, distance, loginMember),
                new NonLoginStrategy(lineExtraFare, distance, loginMember));
    }

    public FarePolicy selectFarePolicy() {
        return userStrategies.stream()
                .filter(UserStrategy::isState)
                .map(UserStrategy::generateFarePolicy)
                .findFirst()
                .orElseThrow(InternalLogicException::new);
    }
}
