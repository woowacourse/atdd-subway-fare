package wooteco.subway.path.domain;

import java.util.List;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.station.domain.Station;

public class SubwayPath {

    private List<Station> stations;
    private SectionEdges sectionEdges;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        this.sectionEdges = new SectionEdges(sectionEdges);
        this.stations = stations;
    }

    public SectionEdges getSectionEdges() {
        return sectionEdges;
    }

    public List<Station> getStations() {
        return stations;
    }

    public int calculateDistance() {
        return sectionEdges.calculateDistance();
    }

    public int calculateFare(int distance) {
        return Fare.calculateFare(distance);
    }

    public int calculateFareWithLine(LoginMember loginMember, int distance) {
        return Fare.calculateFareWithLine(loginMember, distance, sectionEdges.calculateMaxLineFare());
    }
}
