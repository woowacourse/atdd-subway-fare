package wooteco.subway.path.domain;

import java.util.List;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.station.domain.Station;

public class SubwayPath {

    private List<SectionEdge> sectionEdges;
    private List<Station> stations;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        this.sectionEdges = sectionEdges;
        this.stations = stations;
    }

    public Fare fareOf(LoginMember loginMember) {
        int distance = calculateDistance();
        Fare maxExtraFare = calculateMaxExtraFare();

        Fare totalFare = Fare.calculateRate(distance, maxExtraFare);
        return totalFare.discountByAge(loginMember.getAge());
    }

    public int calculateDistance() {
        return sectionEdges.stream()
                .mapToInt(it -> it.getSection().getDistance())
                .sum();
    }

    private Fare calculateMaxExtraFare() {
        int maxExtraFare = sectionEdges.stream()
                .map(SectionEdge::getLine)
                .mapToInt(Line::getExtraFare)
                .max()
                .getAsInt();
        return new Fare(maxExtraFare);
    }

    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }

    public List<Station> getStations() {
        return stations;
    }
}
