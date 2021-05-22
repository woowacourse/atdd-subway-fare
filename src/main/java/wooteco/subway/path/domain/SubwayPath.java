package wooteco.subway.path.domain;

import java.util.List;

import wooteco.subway.station.domain.Station;

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
        int extraFare = findExtraFare();
        if (distance <= 10) {
            return extraFare + DEFAULT_FARE;
        }

        if (distance <= 50) {
            return extraFare + (distance - 10) / 5 * 100 + DEFAULT_FARE;
        }

        return extraFare + (distance - 10) / 8 * 100 + DEFAULT_FARE;
    }

    private int findExtraFare() {
        return sectionEdges.stream()
                           .mapToInt(it -> it.getLine().getExtraFare())
                           .max()
                           .orElse(0);
    }
}
