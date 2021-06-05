package wooteco.subway.fare.domain.distance;

public enum DefaultFare {
    DEFAULT_FARE_TEN_KILO(1250),
    DEFAULT_FARE_FIFTY_KILO(2050);

    private final int defaultFare;

    DefaultFare(int defaultFare) {
        this.defaultFare = defaultFare;
    }

    int value() {
        return defaultFare;
    }
}
