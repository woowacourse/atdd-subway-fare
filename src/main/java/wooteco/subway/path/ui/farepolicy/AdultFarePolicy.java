package wooteco.subway.path.ui.farepolicy;

public class AdultFarePolicy implements FarePolicy {

    public AdultFarePolicy() {
    }

    @Override
    public int discount(int fare) {
        return fare;
    }
}
