package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {
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

    public int calculateFare() {
        int distance = calculateDistance();
        int fare = 1250;
        if (distance <= 10) {
            return fare;
        }

        if (distance > 50) {
            return fare + calculateAdditionalFare(distance - (distance % 50) - 10, 5) + calculateAdditionalFare(distance - 50, 8);
        }

        return fare + calculateAdditionalFare(distance - 10, 5);
    }

    private int calculateAdditionalFare(int distance, int condition) {
        return (int) ((Math.ceil((distance - 1) / condition) + 1) * 100);
    }
}
