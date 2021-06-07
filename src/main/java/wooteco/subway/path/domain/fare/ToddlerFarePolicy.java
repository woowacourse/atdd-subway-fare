package wooteco.subway.path.domain.fare;

public class ToddlerFarePolicy implements FarePolicy {
    @Override
    public double calculateFare(int distance, long extraFare) {
        return 0;
    }
}
