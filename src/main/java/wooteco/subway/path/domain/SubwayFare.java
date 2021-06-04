package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginUser;
import wooteco.subway.member.domain.User;

import java.util.HashSet;
import java.util.Set;

public class SubwayFare {

    private final Set<Line> lines;
    private final Integer distance;
    private final User user;

    public SubwayFare(Set<Line> lines, Integer distance, User user) {
        this.lines = new HashSet<>(lines);
        this.distance = distance;
        this.user = user;
    }

    public int findFare() {
        return AgeFareCalculator.of(
                user.getAge(),
                DistanceFareCalculator.from(distance),
                findExtraFare(lines)
        );
    }

    private int findExtraFare(Set<Line> lines) {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }
}
