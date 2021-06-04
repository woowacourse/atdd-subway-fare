package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.member.domain.User;
import wooteco.subway.path.domain.SubwayRoute;
import wooteco.subway.path.domain.fare.DefaultFarePolicy;
import wooteco.subway.path.domain.fare.FarePolicy;
import wooteco.subway.path.domain.fare.decorator.AgeFarePolicyDecorator;
import wooteco.subway.path.domain.fare.decorator.DistanceFarePolicyDecorator;
import wooteco.subway.path.domain.fare.decorator.ExtraFarePolicyDecorator;

@Service
public class FareService {
    public int calculate(User user, SubwayRoute route) {
        if (user.isLogin()) {
            return loginUserFare(route.distance(), route.extraFare(), user.getAge());
        }
        return guestUserFare(route.distance(), route.extraFare());
    }

    public int loginUserFare(int distance, int extraFare, int age) {
        FarePolicy farePolicy = new AgeFarePolicyDecorator(age, guestFarePolicy(distance, extraFare));
        return farePolicy.calculate();
    }

    public int guestUserFare(int distance, int extraFare) {
        FarePolicy farePolicy = guestFarePolicy(distance, extraFare);
        return farePolicy.calculate();
    }

    private ExtraFarePolicyDecorator guestFarePolicy(int distance, int extraFare) {
        return new ExtraFarePolicyDecorator(extraFare,
                new DistanceFarePolicyDecorator(distance, new DefaultFarePolicy()));
    }
}
