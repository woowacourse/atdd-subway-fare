package wooteco.subway.path.domain;

import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class SubwayPath {

    private static final int DEFAULT_FARE = 1250;

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

    public int calculateFare(int distance, LoginMember loginMember) {
        int extraFare = sectionEdges.stream()
                .mapToInt(it -> it.getLine().getExtraFare())
                .max()
                .getAsInt();

        int totalFare = DEFAULT_FARE + calculateOverFare(distance) + extraFare;

        if (loginMember.getId() == null) {
            return totalFare;
        }

        int discount = 0;
        // 어린이: 운임에서 350원을 공제한 금액의 50%할인
        if (6 <= loginMember.getAge() && loginMember.getAge() < 13) {
            discount = (int) ((totalFare - 350) * 0.5);
        }
        // 청소년: 운임에서 350원을 공제한 금액의 20%할인
        if (13 <= loginMember.getAge() && loginMember.getAge() < 19) {
            discount = (int) ((totalFare - 350) * 0.8);
        }
        return totalFare - discount;
    }

    private int calculateOverFare(int distance) {
        if (distance > 10 && distance <= 50) {
            return (int) ((Math.ceil((distance - 10) / 5) + 1) * 100);
        }
        if (distance > 50) {
            return (int) ((Math.ceil((distance - 50) / 8) + 1) * 100);
        }
        return 0;
    }
}
