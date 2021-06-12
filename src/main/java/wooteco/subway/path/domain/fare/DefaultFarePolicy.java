package wooteco.subway.path.domain.fare;

public class DefaultFarePolicy extends FarePolicy {
    private static final int DEFAULT_FARE = 1250;

    @Override
    public int calculate() {
        return DEFAULT_FARE;
    }
}
