package wooteco.subway.path.domain;

import java.util.List;
import wooteco.subway.exception.FareCalculateException;
import wooteco.subway.member.domain.LoginMember;

public class FareCalculator {

    private static final int DEFAULT_FARE = 1250;

    public int calculate(LoginMember member, int distance, List<SectionEdge> sectionEdges) {
        int fare = DEFAULT_FARE + calculateOverFare(distance, sectionEdges);
        return discountFare(member, fare);
    }

    private int discountFare(LoginMember member, int fare) {
        if (member.getId() == null) {
            return fare;
        }
        if (isTeenager(member)) {
            return (int) (fare - ((fare - 350) * 0.2));
        }
        if (isChild(member)) {
            return (int) (fare - ((fare - 350) * 0.5));
        }
        return fare;
    }

    private boolean isChild(LoginMember member) {
        return member.getAge() >= 6 && member.getAge() < 13;
    }

    private boolean isTeenager(LoginMember member) {
        return member.getAge() >= 13 && member.getAge() < 19;
    }

    private int calculateOverFare(int distance, List<SectionEdge> sectionEdges) {
        return calculateDistanceOverFare(distance) + calculateLineOverFare(sectionEdges);
    }

    private int calculateLineOverFare(List<SectionEdge> sectionEdges) {
        return sectionEdges.stream()
            .mapToInt(edges -> edges.getLine().getFare())
            .max()
            .orElseThrow(FareCalculateException::new)
            ;
    }

    private int calculateDistanceOverFare(int distance) {
        if (distance > 10 && distance <= 50) {
            return (int) ((Math.ceil((distance - 10) / 5) + 1) * 100);
        }
        if (distance > 50) {
            return 800 + (int) ((Math.ceil((distance - 50) / 8) + 1) * 100);
        }
        return 0;
    }
}
