package wooteco.subway.domain;

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
        return sectionEdges.stream()
                .mapToInt(it -> it.getSection()
                        .getDistance())
                .sum();
    }

    public int calculateMaxExtraFare() {
        return sectionEdges.stream()
                .mapToInt(SectionEdge::getExtraFare)
                .max()
                .orElse(0);
    }
}
