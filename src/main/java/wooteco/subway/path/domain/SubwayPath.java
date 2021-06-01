package wooteco.subway.path.domain;

import wooteco.subway.station.domain.Station;

import java.util.List;

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

    public int calculateFare(Integer age) {
        int extraFare = findExtraFare();
        final int fare = DEFAULT_FARE + extraFare + DistanceExtraFarePolicy.apply(calculateDistance());
        return fare - DiscountPolicy.apply(age, fare);
    }

    private int findExtraFare() {
        return sectionEdges.stream()
                           .mapToInt(it -> it.getLine().getExtraFare())
                           .max()
                           .orElse(0);
    }
}
