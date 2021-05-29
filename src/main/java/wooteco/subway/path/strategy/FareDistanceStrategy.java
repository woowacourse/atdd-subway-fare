package wooteco.subway.path.strategy;

public abstract class FareDistanceStrategy implements FareStrategy{
    private final static int TEN_KM = 10;
    private final static int FIFTY_KM = 50;

    protected int extraDistanceFare(int distance) {
        if (distance < TEN_KM)  {
            return 0;
        }

        if (distance < FIFTY_KM) {
            return calculateAdditionalFareOver10km(distance - TEN_KM);
        }

        return calculateAdditionalFareOver10km(FIFTY_KM-TEN_KM) +
                calculateAdditionalFareOver50km(distance-FIFTY_KM);
    }

    protected int calculateAdditionalFareOver10km(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    protected int calculateAdditionalFareOver50km(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
