package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Line;
import wooteco.subway.station.domain.Station;

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
        return sectionEdges.stream().mapToInt(it -> it.getSection().getDistance()).sum();
    }

    public int calculateFare(int age) {
        int distance = calculateDistance();
        int fare = 1250 + extraFare();

        if (distance <= 10) {
            return fare(fare, age);
        }
        if (distance <= 50) {
            return fare(fare + calculateOverFare(distance - 10, 5), age);
        }
        fare += calculateOverFare(40, 5);
        return fare(fare + calculateOverFare(distance - 50, 8), age);
    }

    private int fare(int fare, int age) {
        if (age == 0) {
            return fare;
        }
        if (age < 6) {
            return 0;
        }
        if (age < 13) {
            return (int) ((fare - 350) * 0.5);
        }
        if (age < 19) {
            return (int) ((fare - 350) * 0.8);
        }
        return fare;
    }

    private int calculateOverFare(int distance, int unit) {
        return (int) ((Math.ceil((distance - 1) / unit) + 1) * 100);
    }

    private int extraFare() {
        return sectionEdges.stream().map(SectionEdge::getLine).mapToInt(Line::getExtraFare).max().orElse(0);
    }
}
