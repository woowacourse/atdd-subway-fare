package wooteco.subway.path.domain;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.fare.Fare;
import wooteco.subway.station.domain.Station;

public class SubwayPath {
    private final List<SectionEdge> sectionEdges;
    private final List<Station> stations;
    private final Fare totalFare;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations, Fare totalFare) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
        this.totalFare = totalFare;
    }

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        this(sectionEdges, stations, null);
    }

    public SubwayPath(SubwayPath subwayPathBeforeCalculateFare, Fare totalFare) {
        this(subwayPathBeforeCalculateFare.getSectionEdges(), subwayPathBeforeCalculateFare.stations, totalFare);
    }

    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }

    public List<Line> getLines() {
        return sectionEdges.stream()
            .map(SectionEdge::getLine)
            .distinct()
            .collect(Collectors.toList());
    }

    public int getTotalDistance() {
        return sectionEdges.stream().mapToInt(edge -> edge.getSection().getDistance()).sum();
    }

    public List<Station> getStations() {
        return stations;
    }

    public int getTotalFare() {
        return totalFare.getFare();
    }
}
