package wooteco.subway.path.domain;

public class SubwayFare {

    private static final int BASIC_FARE = 1250;

    private final int value;

    public SubwayFare(SubwayPath subwayPath, int passengerAge) {
        this.value = fare(subwayPath, passengerAge);
    }

    private int fare(SubwayPath subwayPath, int passengerAge) {
        int fare = BASIC_FARE;

        fare = DistanceFarePolicy.distancePolicyAppliedFare(fare, subwayPath.distance());
        fare += subwayPath.lineExtraFare();
        fare = AgeFarePolicy.agePolicyAppliedFare(fare, passengerAge);

        return fare;
    }

    public int getValue() {
        return value;
    }
}
