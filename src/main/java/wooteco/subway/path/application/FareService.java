package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.path.domain.fare.DefaultFarePolicy;
import wooteco.subway.path.domain.fare.FarePolicy;
import wooteco.subway.path.domain.fare.decorator.AgeFarePolicyDecorator;
import wooteco.subway.path.domain.fare.decorator.DistanceFarePolicyDecorator;
import wooteco.subway.path.domain.fare.decorator.ExtraFarePolicyDecorator;

@Service
public class FareService {
    public int calculate(int distance, int extraFare) {
        FarePolicy farePolicy =
                new ExtraFarePolicyDecorator(extraFare,
                        new DistanceFarePolicyDecorator(distance,
                                new DefaultFarePolicy()));
        return farePolicy.calculate();
    }

    public int calculate(int distance, int extraFare, int age) {
        FarePolicy farePolicy =
                new AgeFarePolicyDecorator(age,
                        new ExtraFarePolicyDecorator(extraFare,
                                new DistanceFarePolicyDecorator(distance,
                                        new DefaultFarePolicy())));
        return farePolicy.calculate();
    }
}
