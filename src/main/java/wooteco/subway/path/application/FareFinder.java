package wooteco.subway.path.application;

import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.AgeFareCalculator;
import wooteco.subway.path.domain.DistanceFareCalculator;
import wooteco.subway.path.domain.SubwayFare;

import java.util.Set;

public class FareFinder {

    public static SubwayFare findFare(Set<Line> lines, int distance, LoginMember loginMember) {
        int fare = AgeFareCalculator.of(
                loginMember.getAge(),
                DistanceFareCalculator.from(distance),
                findExtraFare(lines)
        );
        return new SubwayFare(fare);
    }

    private static int findExtraFare(Set<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }
}
