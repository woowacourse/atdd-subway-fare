package wooteco.subway.path.domain;

import wooteco.subway.path.ui.farepolicy.FarePolicy;
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

    public int getExtraFare(FarePolicy farePolicy) {
        int distance = calculateDistance();
        int maxExtraLineFare = sectionEdges.stream()
                .mapToInt(sectionEdges -> sectionEdges.getLine().getExtraFare())
                .max()
                .orElse(0);

        Fare fare = new Fare(farePolicy);
        return fare.calculateTotalFare(distance, maxExtraLineFare);
    }

    public int calculateDistance() {
        return sectionEdges.stream()
                .mapToInt(it -> it.getSection().getDistance()).sum();
    }
}
