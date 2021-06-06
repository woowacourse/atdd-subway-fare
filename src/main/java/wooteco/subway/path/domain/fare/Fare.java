package wooteco.subway.path.domain.fare;

public class Fare {
    private final int fare;

    public Fare(int distance, int extraFare, int age) {
        fare = calculateFare(distance, extraFare, age);
    }

    public int calculateFare(int distance, int extraFare, int age) {
        int fareAfterDistanceDiscount = DistanceFareRule.calculateFee(distance);
        return AgeDiscount.calculateFareAfterDiscount(fareAfterDistanceDiscount, age) + extraFare;
    }

    public int getFare() {
        return fare;
    }
}
