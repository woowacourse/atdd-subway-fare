package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {
    private static final int DEFAULT_FARE = 1250;
    private static final int FIRST_BOUND = 10;
    private static final int SECOND_BOUND = 50;
    private static final int ZERO = 0;

    private List<SectionEdge> sectionEdges;
    private List<Station> stations;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
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

    public int calculateFare(int distance) {
        if (distance > SECOND_BOUND) {
            return DEFAULT_FARE + plusFirstBound(SECOND_BOUND) + plusSecondBound(distance);
        }
        return DEFAULT_FARE + plusFirstBound(distance);
    }

    private int plusFirstBound(int distance) {
        if (distance < FIRST_BOUND) {
            return ZERO;
        }
        return (int) (Math.ceil((distance - FIRST_BOUND) / 5.0) * 100);
    }

    private int plusSecondBound(int distance) {
        return (int) (Math.ceil((distance - SECOND_BOUND) / 8.0) * 100);
    }
}