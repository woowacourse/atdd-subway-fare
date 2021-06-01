package wooteco.subway.path.domain;

public class SubwayFare {

    private static final int BASIC_FARE = 1250;

    private final SubwayPath subwayPath;
    private final int passengerAge;

    public SubwayFare(SubwayPath subwayPath, int passengerAge) {
        this.subwayPath = subwayPath;
        this.passengerAge = passengerAge;
    }

    public int value() {
        int fare = BASIC_FARE;

        fare = DistanceFarePolicy.distancePolicyAppliedFare(fare, subwayPath.distance());
        fare += subwayPath.lineExtraFare();
        fare = AgeFarePolicy.agePolicyAppliedFare(fare, passengerAge);

        return fare;
    }

}
