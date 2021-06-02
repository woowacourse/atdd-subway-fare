package wooteco.subway.path.domain.fare;

public class Fare {
    private static final ChainAssembler calculator = new ChainAssembler();

    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare of(int distance, int extraFare) {
        return new Fare(calculator.calculateByDistance(distance) + extraFare);
    }

    public int toInt() {
        return fare;
    }
}
