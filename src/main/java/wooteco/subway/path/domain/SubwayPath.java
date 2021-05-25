package wooteco.subway.path.domain;

import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {

    private List<SectionEdge> sectionEdges;
    private List<Station> stations;
    private FareCalculator fareCalculator;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
        this.fareCalculator = new FareCalculator();
    }

    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int calculateDistance() {
        return sectionEdges.stream()
            .mapToInt(it -> it.getSection().getDistance())
            .sum();
    }

    public int calculateFare(LoginMember member, int distance) {
        return fareCalculator.calculate(member, distance, sectionEdges);
    }


}
