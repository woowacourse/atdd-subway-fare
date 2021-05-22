package wooteco.subway.path.domain;

import java.util.List;
import wooteco.subway.member.domain.AuthMember;
import wooteco.subway.station.domain.Station;

public class SubwayPath {

    private static final int DEFAULT_DISTANCE = 10;
    private static final int OVER_LIMIT_DISTANCE = 50;
    private static final int DEFAULT_FARE = 1250;

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

    public int calculateDistance() {
        return sectionEdges.stream().mapToInt(it -> it.getSection().getDistance()).sum();
    }

    public int calculateFare(int distance, AuthMember authMember) {
        int fare = DEFAULT_FARE;
        fare += calculateAdditionalFareByDistance(distance);
        fare += calculateAdditionalFareByLine();
        fare = authMember.discountFareByAge(fare);

        return fare;
    }

    private int calculateAdditionalFareByDistance(int distance) {
        int fare = 0;
        if (DEFAULT_DISTANCE < distance && distance <= OVER_LIMIT_DISTANCE) {
            return calculateAdditionalFareOver10km(distance - DEFAULT_DISTANCE);
        }

        if (OVER_LIMIT_DISTANCE < distance) {
            return calculateAdditionalFareOver10km(OVER_LIMIT_DISTANCE - DEFAULT_DISTANCE)
                + calculateAdditionalFareOver50km(distance - OVER_LIMIT_DISTANCE);
        }
        return fare;
    }

    private int calculateAdditionalFareOver10km(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private int calculateAdditionalFareOver50km(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

    private int calculateAdditionalFareByLine() {
        return sectionEdges.stream()
            .mapToInt(SectionEdge::getExtraFare)
            .max()
            .getAsInt();
    }
}
