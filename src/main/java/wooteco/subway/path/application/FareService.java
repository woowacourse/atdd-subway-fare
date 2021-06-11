package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.domain.Lines;
import wooteco.subway.path.domain.AgePolicy;
import wooteco.subway.path.domain.DistancePolicy;
import wooteco.subway.path.domain.Fare;
import wooteco.subway.section.domain.Distance;

@Service
public class FareService {

    public FareService() {
    }

    public Fare calculateFare(Distance distance) {
        return new Fare(DistancePolicy.calculateFare(distance.getValue()));
    }

    public Fare calculateExtraFare(Fare fare, Lines lines) {
        return fare.addExtraFare(lines.getMaxExtraFare());
    }

    public Fare discountFareByAge(int age, int fare) {
        AgePolicy agePolicy = AgePolicy.of(age);
        return new Fare(agePolicy.discount(fare));
    }
}
