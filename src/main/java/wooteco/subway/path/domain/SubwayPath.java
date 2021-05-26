package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {
    private List<SectionEdge> sectionEdges;
    private List<Station> stations;
    private int extraFare;

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

    public int getExtraFare() {
        int distance = calculateDistance();
        if (distance <= 10) {
            return 1250;
        }
        if (distance <= 50) {
            distance = distance - 10;
            return 1250 + (((distance - 1) / 5 + 1) * 100);
        }

        distance = distance - 50;
        return 1250 + 800 + (((distance - 1) / 8 + 1) * 100);
    }

    public int calculateDistance() {
        return sectionEdges.stream()
                .mapToInt(it -> it.getSection().getDistance()).sum();
    }
}
