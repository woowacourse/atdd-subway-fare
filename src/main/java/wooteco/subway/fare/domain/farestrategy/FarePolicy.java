package wooteco.subway.fare.domain.farestrategy;

import wooteco.subway.fare.domain.Money;

import java.util.Arrays;
import java.util.List;

public class FarePolicy {
    private final List<FareStrategy> policies;

    public FarePolicy(FareStrategy... strategies) {
        this.policies = Arrays.asList(strategies);
    }

    public Money calculateTotalFare(Money value) {
        Money result = value;
        for (FareStrategy policy : policies) {
            result = policy.calculate(result);
        }
        return result;
    }
}
