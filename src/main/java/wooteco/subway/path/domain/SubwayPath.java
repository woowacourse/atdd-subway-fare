package wooteco.subway.path.domain;

import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {
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

    public Distance calculateTotalDistance() {
        return sectionEdges.stream()
                .map(SectionEdge::getDistance)
                .reduce(Distance::add)
                .orElseThrow(IllegalDistanceException::new);
    }

    public Fare calculateFare(Distance distance, LoginMember loginMember) {
        Fare totalFare = Fare.calculateTotalFare(sectionEdges, distance);
        return totalFare.calculateFareAfterDiscount(loginMember);
    }
}
