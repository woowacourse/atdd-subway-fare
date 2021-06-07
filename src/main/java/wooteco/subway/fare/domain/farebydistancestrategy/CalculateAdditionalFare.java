package wooteco.subway.fare.domain.farebydistancestrategy;

public class CalculateAdditionalFare {

    public static int calculateAdditionalFareOver10km(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    public static int calculateAdditionalFareOver50km(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
