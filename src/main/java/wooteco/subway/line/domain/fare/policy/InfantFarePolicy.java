package wooteco.subway.line.domain.fare.policy;

public class InfantFarePolicy implements FarePolicy {

    public InfantFarePolicy() {
    }

    @Override
    public int discount(int fare) {
        return 0;
    }
}
