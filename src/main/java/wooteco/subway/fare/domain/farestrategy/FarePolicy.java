package wooteco.subway.fare.domain.farestrategy;

import wooteco.subway.fare.domain.Money;

import java.util.ArrayList;
import java.util.List;

public class FarePolicy {
    private final List<FareStrategy> policies;

    private FarePolicy(Builder builder) {
        policies = builder.policies;
    }

    public Money calculateTotalFare(Money value) {
        Money result = value;
        for (FareStrategy policy : policies) {
            result = policy.calculate(result);
        }
        return result;
    }

    public static class Builder {
        private final List<FareStrategy> policies;

        public Builder() {
            this.policies = new ArrayList<>();
        }

        public Builder distancePolicy(DistanceFare distanceFare) {
            policies.add(distanceFare);
            return this;
        }


        public Builder agePolicy(AgeFare ageFare) {
            policies.add(ageFare);
            return this;
        }

        public Builder linePolicy(LineFare lineFare) {
            policies.add(lineFare);
            return this;
        }

        public FarePolicy build() {
            return new FarePolicy(this);
        }
    }
}
