package wooteco.subway.line.domain.fare.policy;

public class AdultFarePolicy implements FarePolicy {

    public AdultFarePolicy() {
    }

    @Override
    public int discount(int fare) {
        return fare;
    }
}
