package wooteco.subway.util;

public enum FarePolicy {
    DEFAULT_FARE(1250),
    ADDITIONAL_FARE(100),
    MINOR_DISCOUNT_FARE(350),
    DEFAULT_DISCOUNT_FARE(0);

    private final int fare;

    FarePolicy(final int fare) {
        this.fare = fare;
    }

    public int getFare() {
        return fare;
    }
}
