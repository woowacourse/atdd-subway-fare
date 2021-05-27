package wooteco.subway.path.ui.farepolicy;

public class InfantFarePolicy implements FarePolicy {

    public InfantFarePolicy() {
    }

    @Override
    public int discount(int fare) {
        return 0;
    }
}
