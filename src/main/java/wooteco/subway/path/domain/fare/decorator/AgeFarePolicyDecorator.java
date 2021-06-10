package wooteco.subway.path.domain.fare.decorator;

import wooteco.subway.path.domain.fare.FarePolicy;

public class AgeFarePolicyDecorator extends FarePolicyDecorator {
    private final int age;

    public AgeFarePolicyDecorator(int age, FarePolicy farePolicy) {
        super(farePolicy);
        this.age = age;
    }

    @Override
    public int calculate() {
        return AgeFarePolicy.of(age).calculate(super.calculate());
    }
}
