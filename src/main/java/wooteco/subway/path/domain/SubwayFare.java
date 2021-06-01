package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Line;
import wooteco.subway.path.strategy.FareStrategy;

import java.util.Set;

public class SubwayFare {

    private final Set<Line> lines;
    private final FareStrategy fareStrategy;

    public SubwayFare(Set<Line> lines, FareStrategy fareStrategy) {
        this.lines = lines;
        this.fareStrategy = fareStrategy;
    }

    public int calculateFare(int distance) {
        return fareStrategy.discount(distance, findExtraFare());
    }

    private int findExtraFare() {
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
    }


}
