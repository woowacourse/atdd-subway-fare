package wooteco.subway.path.domain.fare;

public class ToddlerFarePrincipal implements FarePrincipal {
    @Override
    public double calculateFare(int distance, long extraFare) {
        return 0;
    }
}
