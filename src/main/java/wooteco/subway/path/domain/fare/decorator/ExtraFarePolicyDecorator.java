package wooteco.subway.path.domain.fare.decorator;

import wooteco.subway.path.domain.fare.FarePolicy;

public class ExtraFarePolicyDecorator extends FarePolicyDecorator {
    int extraFare;

    public ExtraFarePolicyDecorator(int extraFare, FarePolicy farePolicy) {
        super(farePolicy);
        this.extraFare = extraFare;
    }

    @Override
    public int calculate() {
        return super.calculate() + extraFare;
    }
}
