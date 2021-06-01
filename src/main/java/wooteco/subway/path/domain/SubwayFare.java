package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;

import java.util.HashSet;
import java.util.Set;

public class SubwayFare {

    private final Set<Line> lines;
    private final Integer distance;
    private final LoginMember loginMember;

    public SubwayFare(Set<Line> lines, Integer distance, LoginMember loginMember) {
        this.lines = new HashSet<>(lines);
        this.distance = distance;
        this.loginMember = loginMember;
    }

    public int findFare() {
        System.out.println(AgeFareCalculator.of(loginMember.getAge(), DistanceFareCalculator.from(distance), findExtraFare(lines)));

        return AgeFareCalculator.of(
                loginMember.getAge(),
                DistanceFareCalculator.from(distance),
                findExtraFare(lines)
        );
    }

    private int findExtraFare(Set<Line> lines) {

        return lines.stream()
                .peek(line -> System.out.println(line.getName()))
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }
}
