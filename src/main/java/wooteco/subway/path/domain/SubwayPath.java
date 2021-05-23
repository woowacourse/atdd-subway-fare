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
        long extraFare = findExtraFare();
        int baseFare = BASIC_FARE + (int) extraFare;

        if (distance <= FIRST_OVER_FARE_DISTANCE) {
            return baseFare;
        }

        if (distance <= SECOND_OVER_FARE_DISTANCE) {
            return calculateOverFare(baseFare, distance - FIRST_OVER_FARE_DISTANCE, 5);
        }

        baseFare = OVER_FARE + (int) extraFare;
        return calculateOverFare(baseFare, distance - SECOND_OVER_FARE_DISTANCE, 8);
    }


    private Long findExtraFare() {
        return sectionEdges.stream()
                .mapToLong(SectionEdge::extraFare)
                .max()
                .orElse(0);
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
