package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {
    private static final int BASIC_FARE = 1250;
    private static final int OVER_FARE = 2050;
    private static final int FARE_PER_KM = 100;
    private static final int FIRST_OVER_FARE_DISTANCE = 10;
    private static final int SECOND_OVER_FARE_DISTANCE = 50;

    private List<SectionEdge> sectionEdges;
    private List<Station> stations;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
    }

    public int calculateFare() {
        int distance = calculateDistance();

        if (distance <= FIRST_OVER_FARE_DISTANCE) {
            return BASIC_FARE;
        }

        if (distance <= SECOND_OVER_FARE_DISTANCE) {
            return calculateOverFare(BASIC_FARE, distance - FIRST_OVER_FARE_DISTANCE, 5);
        }
        return calculateOverFare(OVER_FARE, distance - SECOND_OVER_FARE_DISTANCE, 8);
    }

    private int calculateOverFare(int baseFare, int distance, int perDistance) {
        return baseFare + (int) (Math.ceil(distance / perDistance) * FARE_PER_KM);
    }

    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int calculateDistance() {
        return sectionEdges.stream().mapToInt(it -> it.getSection().getDistance()).sum();
    }
}
