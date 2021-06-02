package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {
    private static final int DEFAULT_EXTRA_FARE = 0;

    private final List<SectionEdge> sectionEdges;
    private final List<Station> stations;

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

    public int getMaximumExtraFareByLine(){
        return sectionEdges.stream()
                .mapToInt(SectionEdge::getExtraFareByLine)
                .max()
                .orElse(DEFAULT_EXTRA_FARE);
    }

    public int getDistance() {
        return sectionEdges.stream().mapToInt(it -> it.getSection().getDistance()).sum();
    }
}
