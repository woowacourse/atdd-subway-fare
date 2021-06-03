package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.path.domain.Fare;
import wooteco.subway.path.domain.policy.BasicLevelPolicy;
import wooteco.subway.path.domain.policy.Policy;
import wooteco.subway.path.domain.policy.SecondLevelPolicy;
import wooteco.subway.path.domain.policy.ThirdLevelPolicy;

@Service
public class FareService {

    private final Policy policy;

    public FareService() {
        this.policy = new BasicLevelPolicy();
        SecondLevelPolicy secondLevelPolicy = new SecondLevelPolicy();
        ThirdLevelPolicy thirdLevelPolicy = new ThirdLevelPolicy();

        this.policy.setNextLevelPolicy(secondLevelPolicy);
        secondLevelPolicy.setNextLevelPolicy(thirdLevelPolicy);
    }

    public Fare calculateFare(int distance) {
        return new Fare(policy.calculateTotalFare(distance));
    }
}
