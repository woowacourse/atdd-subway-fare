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
        int totalDistance = calculateDistance();
        if (totalDistance > 50) {
            return 1250 + 800 + (int) ((Math.ceil((totalDistance - 51) / 8)) + 1) * 100;
        }
        if (10 < totalDistance && totalDistance <= 50) {
            return 1250 + (int) ((Math.ceil((totalDistance - 11) / 5)) + 1) * 100;
        }
        return 1250;
    }
}
