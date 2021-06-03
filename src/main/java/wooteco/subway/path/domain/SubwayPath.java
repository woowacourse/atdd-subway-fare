package wooteco.subway.path.domain;

import java.util.List;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

public class SubwayPath {

    private List<SectionEdge> sectionEdges;
    private List<Station> stations;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
    }

    public int calculateDistance() {
        return sectionEdges.stream()
            .map(SectionEdge::getSection)
            .mapToInt(Section::getDistance)
            .sum();
    }

    public Fare calculateMaxExtraFare() {
        int maxExtraFare = sectionEdges.stream()
            .map(SectionEdge::getLine)
            .mapToInt(Line::getExtraFare)
            .max()
            .getAsInt();
        return new Fare(maxExtraFare);
    }

    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }

    public List<Station> getStations() {
        return stations;
    }
}
