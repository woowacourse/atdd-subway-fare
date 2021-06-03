package wooteco.subway.line.domain.fare.distance;

public enum MaxDistance {
    TEN_KM(10),
    FIFTY_KM(50),
    OVER_FIFTY_KM(Integer.MAX_VALUE);

    private final int distance;

    MaxDistance(int distance) {
        this.distance = distance;
    }

    int value() {
        return distance;
    }
}
