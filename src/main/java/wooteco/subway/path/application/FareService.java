package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.domain.Lines;
import wooteco.subway.path.domain.Fare;
import wooteco.subway.path.domain.policy.AdultAgePolicy;
import wooteco.subway.path.domain.policy.AgePolicy;
import wooteco.subway.path.domain.policy.BasicDistancePolicy;
import wooteco.subway.path.domain.policy.ChildAgePolicy;
import wooteco.subway.path.domain.policy.DistancePolicy;
import wooteco.subway.path.domain.policy.SecondDistancePolicy;
import wooteco.subway.path.domain.policy.TeenagerAgePolicy;
import wooteco.subway.path.domain.policy.ThirdDistancePolicy;
import wooteco.subway.section.domain.Distance;

@Service
public class FareService {

    private final DistancePolicy distancePolicy;
    private final AgePolicy agePolicy;

    public FareService() {
        this.distancePolicy = new BasicDistancePolicy();
        SecondDistancePolicy secondDistancePolicy = new SecondDistancePolicy();
        ThirdDistancePolicy thirdDistancePolicy = new ThirdDistancePolicy();
        this.distancePolicy.setNextPolicy(secondDistancePolicy);
        secondDistancePolicy.setNextPolicy(thirdDistancePolicy);

        this.agePolicy = new AdultAgePolicy();
        TeenagerAgePolicy teenagerAgePolicy = new TeenagerAgePolicy();
        ChildAgePolicy childAgePolicy = new ChildAgePolicy();
        this.agePolicy.setNextPolicy(teenagerAgePolicy);
        teenagerAgePolicy.setNextPolicy(childAgePolicy);
    }

    public Fare calculateFare(Distance distance) {
        return new Fare(distancePolicy.calculateFare(distance.getValue()));
    }

    public Fare calculateExtraFare(Fare fare, Lines lines) {
        return fare.addExtraFare(lines.getMaxExtraFare());
    }

    public Fare discountFareByAge(int age, int fare) {
        return new Fare(agePolicy.calculateFare(age, fare));
    }
}
