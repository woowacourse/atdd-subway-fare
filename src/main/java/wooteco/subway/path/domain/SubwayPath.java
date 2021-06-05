package wooteco.subway.path.domain;

import wooteco.subway.path.domain.fare.FarePrincipal;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {
    private List<SectionEdge> sectionEdges;
    private List<Station> stations;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
    }

    public double calculateFare(FarePrincipal farePrincipal) {
        return farePrincipal.calculateFare(calculateDistance(), findExtraFare());
    }

    private Long findExtraFare() {
        return sectionEdges.stream()
                .mapToLong(SectionEdge::extraFare)
                .max()
                .orElse(0);
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
