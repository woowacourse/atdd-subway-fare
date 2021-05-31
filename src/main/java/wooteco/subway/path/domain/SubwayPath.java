package wooteco.subway.path.domain;

import java.util.List;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

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

    public Price calculatePrice() {
        int distance = calculateDistance();
        // 노선 추가요금 로직 필요
        int maxExtraFare = sectionEdges.stream()
                .map(SectionEdge::getLine)
                .mapToInt(Line::getExtraFare)
                .max()
                .getAsInt();

        return Price.calculateRate(distance).add(new Price(maxExtraFare));
    }
}
