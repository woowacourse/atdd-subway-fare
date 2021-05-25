package wooteco.subway.path.domain;

import java.math.BigDecimal;
import java.util.List;
import wooteco.subway.path.domain.farepolicy.FarePolicy;

public class Fare {

    private final List<FarePolicy> farePolicies;

    public Fare(List<FarePolicy> farePolicies) {
        this.farePolicies = farePolicies;
    }

    public BigDecimal calculate(SubwayPath subwayPath) {
        int distance = subwayPath.calculateDistance();

        return farePolicies.stream().map(
            farePolicy -> farePolicy.calculate(distance)
        ).reduce(BigDecimal.valueOf(0), BigDecimal::add);
    }

}
