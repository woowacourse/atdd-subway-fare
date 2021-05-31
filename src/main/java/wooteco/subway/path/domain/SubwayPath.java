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

    public int calculateDistance() {
        return sectionEdges.stream().mapToInt(it -> it.getSection().getDistance()).sum();
    }

    public Fare calculatePrice() {
        int distance = calculateDistance();

        int maxExtraFare = sectionEdges.stream()
                .map(SectionEdge::getLine)
                .mapToInt(Line::getExtraFare)
                .max()
                .getAsInt();

        return Fare.calculateRate(distance, maxExtraFare);
    }

    public Fare fareOf(LoginMember loginMember) {
        Fare fare = calculatePrice();
        Fare discountFare = fare.discountByAge(loginMember.getAge());
        return fare.subtract(discountFare);
    }

    public List<SectionEdge> getSectionEdges() {
        return sectionEdges;
    }

    public List<Station> getStations() {
        return stations;
    }
}
