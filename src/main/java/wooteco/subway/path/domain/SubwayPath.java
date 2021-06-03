package wooteco.subway.path.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import wooteco.subway.fare.domain.farebydistancestrategy.FareByDistance;
import wooteco.subway.member.domain.authmember.AuthMember;
import wooteco.subway.station.domain.Station;

public class SubwayPath {

    private static final int DEFAULT_FARE = 1250;

    private final List<SectionEdge> sectionEdges;
    private final List<Station> stations;

    public SubwayPath(List<SectionEdge> sectionEdges, List<Station> stations) {
        this.sectionEdges = new ArrayList<>(sectionEdges);
        this.stations = new ArrayList<>(stations);
    }

    public List<SectionEdge> getSectionEdges() {
        return Collections.unmodifiableList(sectionEdges);
    }

    public List<Station> getStations() {
        return Collections.unmodifiableList(stations);
    }

    public int calculateDistance() {
        return sectionEdges.stream().mapToInt(it -> it.getSection().getDistance()).sum();
    }

    public int calculateFare(int distance, AuthMember authMember) {
        int fare = DEFAULT_FARE;
        fare += calculateAdditionalFare(distance);
        fare += calculateAdditionalFareByLine();
        fare = authMember.discountFareByAge(fare);
        return fare;
    }

    private int calculateAdditionalFare(int distance) {
        FareByDistance fareByDistance = new FareByDistance(distance);
        return fareByDistance.calculateAdditionalFare();
    }

    private int calculateAdditionalFareByLine() {
        return sectionEdges.stream()
            .mapToInt(SectionEdge::getExtraFare)
            .max()
            .getAsInt();
    }
}
