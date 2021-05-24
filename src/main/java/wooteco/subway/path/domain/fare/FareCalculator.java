package wooteco.subway.path.domain.fare;

import java.util.Comparator;
import java.util.List;
import wooteco.subway.line.domain.Line;

public class FareCalculator {
    public Fare getFareWithLineExtraFare(Fare currentFare, List<Line> lines) {
        return lines.stream()
            .map(Line::getExtraFare)
            .max(Comparator.comparingInt(Fare::getFare))
            .orElseThrow(() -> new IllegalArgumentException("Line이 한 개 이상 존재해야 합니다."))
            .add(currentFare);
    }
}
