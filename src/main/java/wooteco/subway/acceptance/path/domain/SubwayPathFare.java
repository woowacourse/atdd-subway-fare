package wooteco.subway.acceptance.path.domain;

public class SubwayPathFare {
    private final int age;
    private final int distance;
    private final int lineFare;

    public SubwayPathFare(int age, int distance, int lineFare) {
        this.age = age;
        this.distance = distance;
        this.lineFare = lineFare;
    }

    public int getFare() {
        int fareByDistance = FareCalculatorByDistance.from(distance);
        return FareAdjusterByAge.of(age, fareByDistance, lineFare);
    }
}
