package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {

    private static final int DEFAULT_FARE = 1250;

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
        return DEFAULT_FARE + calculateOverFare(distance);
    }

    private int calculateOverFare(int distance) {
        if(distance > 10 && distance <= 50) {
            return (int) ((Math.ceil((distance -10) / 5) +1) * 100);
        }
        if(distance > 50) {
            return (int) ((Math.ceil((distance -50) / 8) +1) * 100);
        }
        return 0;
    }
}
