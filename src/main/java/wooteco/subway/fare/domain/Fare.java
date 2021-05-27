package wooteco.subway.fare.domain;

public class Fare {
    private static final int DEFAULT_FARE = 1250;

    private final int totalDistance;
    private final int lineExtraFare;

    public Fare(int totalDistance, int lineExtraFare) {
        this.totalDistance = totalDistance;
        this.lineExtraFare = lineExtraFare;
    }

    public int calculateBasicFare() {
        int basicFare = DEFAULT_FARE + lineExtraFare;
        if (totalDistance <= 10) {
            return basicFare;
        }
        if (totalDistance <= 50) {
            return basicFare + calculateExtraFare(totalDistance - 10, 5);
        }
        return basicFare + calculateExtraFare(40, 5) +
                calculateExtraFare(totalDistance - 50, 8);
    }

    private int calculateExtraFare(int distance, int unitDistance) {
        return (int) Math.ceil(((double) (distance)) / unitDistance) * 100;
    }

    public int calculateDiscountFare(int age) {
        if (age < 6) {
            return 0;
        }
        if (age >= 19) {
            return calculateBasicFare();
        }
        if (age < 13) {
            return (calculateBasicFare() - 350) / 2;
        }
        return (int) ((calculateBasicFare() - 350) * 0.8);
    }
}
