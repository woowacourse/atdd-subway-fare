package wooteco.subway.path.domain;

import wooteco.subway.fare.domain.AgeFarePolicy;
import wooteco.subway.fare.domain.Fare;
import wooteco.subway.station.domain.Station;

import java.util.List;

public final class SubwayPath {
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

    public int getExtraFare(AgeFarePolicy ageFarePolicy) {
        int distance = calculateDistance();
        int maxExtraLineFare = sectionEdges.stream()
                .mapToInt(sectionEdges -> sectionEdges.getLine().getExtraFare())
                .max()
                .orElse(0);

        Fare fare = new Fare(ageFarePolicy);
        return fare.calculateTotalFare(distance, maxExtraLineFare);
    }

    public int calculateDistance() {
        return sectionEdges.stream()
                .mapToInt(it -> it.getSection().getDistance()).sum();
    }
}
