package wooteco.subway.util;

public enum FarePolicy {
    DEFAULT_FARE(1250),
    ADDITIONAL_FARE(100),
    MINOR_DISCOUNT_FARE(350);

    private final int fare;

    FarePolicy(final int fare) {
        this.fare = fare;
    }

    public int getFare(){
        return fare;
    }
}
