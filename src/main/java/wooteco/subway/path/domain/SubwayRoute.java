package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayRoute {
    private final List<Station> stations;

    private final int distance;
    private final int extraFare;

    public SubwayRoute(List<SectionEdge> sectionEdges, List<Station> stations) {
        this.stations = stations;
        this.distance = sectionEdges.stream()
                .mapToInt(it -> it.getSection()
                        .getDistance())
                .sum();
        this.extraFare = sectionEdges.stream()
                .mapToInt(it -> it.getLine()
                        .getExtraFare())
                .max()
                .orElse(0);
    }

    public List<Station> stations() {
        return stations;
    }

    public int distance() {
        return distance;
    }

    public int extraFare() {
        return extraFare;
    }
}

