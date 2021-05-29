package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Line;
import wooteco.subway.path.strategy.FareStrategy;
import wooteco.subway.station.domain.Station;

import java.util.List;
import java.util.Set;

public class SubwayPath {
    private static final int DEFAULT_DISTANCE = 10;
    private static final int OVER_LIMIT_DISTANCE = 50;
    private final int DEFAULT_FARE = 1250;

    private final List<SectionEdge> sectionEdges;
    private final List<Station> stations;
    private final Set<Line> lines;
    private final FareStrategy fareStrategy;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations, Set<Line> lines, FareStrategy fareStrategy) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
        this.lines = lines;
        this.fareStrategy = fareStrategy;
    }

    public int calculateFare(int distance) {
        return fareStrategy.discount(calculateDistance(), findExtraFare());
    }

    private int findExtraFare() {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }

    public int calculateDistance() {
        return sectionEdges.stream().mapToInt(it -> it.getSection().getDistance()).sum();
    }

    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }

    public List<Station> getStations() {
        return stations;
    }

}
