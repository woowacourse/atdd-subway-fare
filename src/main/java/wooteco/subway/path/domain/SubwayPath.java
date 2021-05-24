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
        int extraFare = getExtraFare();

        int totalFare = DEFAULT_FARE + calculateOverFare(distance) + extraFare;

        if (loginMember.getId() == null) {
            return totalFare;
        }

        int discount = getDiscount(loginMember, totalFare);
        return totalFare - discount;
    }

    private int getExtraFare() {
        return sectionEdges.stream()
                .mapToInt(it -> it.getLine().getExtraFare())
                .max()
                .getAsInt();
    }

    private int getDiscount(final LoginMember loginMember, final int totalFare) {
        int age = loginMember.getAge();
        int discount = 0;
        // 어린이: 운임에서 350원을 공제한 금액의 50%할인
        if (6 <= age && age < 13) {
            discount = (int) ((totalFare - 350) * 0.5);
        }
        // 청소년: 운임에서 350원을 공제한 금액의 20%할인
        if (13 <= age && age < 19) {
            discount = (int) ((totalFare - 350) * 0.2);
        }
        return discount;
    }

    private int calculateOverFare(int distance) {
        if (distance > 10 && distance <= 50) {
            return (int) ((Math.ceil((distance - 10) / 5) + 1) * 100);
        }
        if (distance > 50) {
            return 800 + (int) ((Math.ceil((distance - 50) / 8) + 1) * 100);
        }
        return 0;
    }
}
