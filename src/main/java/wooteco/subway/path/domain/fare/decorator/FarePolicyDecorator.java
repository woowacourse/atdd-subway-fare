package wooteco.subway.path.domain.fare.decorator;

import wooteco.subway.path.domain.fare.FarePolicy;

public abstract class FarePolicyDecorator extends FarePolicy {
    private FarePolicy farePolicy;

    public FarePolicyDecorator(FarePolicy farePolicy) {
        this.farePolicy = farePolicy;
    }

    @Override
    public int calculate() {
        return farePolicy.calculate();
    }
}
