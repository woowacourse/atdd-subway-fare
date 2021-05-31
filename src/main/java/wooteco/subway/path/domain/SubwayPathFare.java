package wooteco.subway.path.domain;

public class SubwayPathFare {
    private final Integer age;
    private final int distance;
    private final int lineFare;

    public SubwayPathFare(Integer age, int distance, int lineFare) {
        this.age = wrapAge(age);
        this.distance = distance;
        this.lineFare = lineFare;
    }

    private int wrapAge(Integer age) {
        if (age == null) {
            return -1;
        }
        return age;
    }

    public int getFare() {
        int fareByDistance = FareCalculatorByDistance.from(distance);
        return FareAdjusterByAge.of(age, fareByDistance, lineFare);
    }
}
