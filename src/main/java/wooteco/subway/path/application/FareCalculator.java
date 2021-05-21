package wooteco.subway.path.application;

import java.util.Comparator;
import java.util.List;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.Fare;

public class FareCalculator {
    public Fare getExtraFareByDistance(Fare currentFare, int distance) {
        if (distance <= 10) {
            return currentFare;
        }
        if (distance <= 50) {
            int extraDistance = distance - 10;
            int extraFare = (int) (Math.ceil(extraDistance / 5.0) * 100);
            return getExtraFareByDistance(currentFare, 10).add(new Fare(extraFare));
        }
        int extraDistance = distance - 50;
        int extraFare = (int) (Math.ceil(extraDistance / 8.0) * 100);
        return getExtraFareByDistance(currentFare, 50).add(new Fare(extraFare));
    }


    public Fare getFareWithLineExtraFare(Fare currentFare, List<Line> lines) {
        return lines.stream()
            .map(Line::getFare)
            .max(Comparator.comparingInt(Fare::getFare))
            .orElseThrow(() -> new IllegalArgumentException("Line이 한 개 이상 존재해야 합니다."))
            .add(currentFare);
    }

    public Fare getFareByAge(int age, Fare fare) {
        if (age < 6) {
            return new Fare(0);
        }
        if (age < 13) {
            return fare.sub(new Fare(350)).discount(0.5);
        }
        if (age < 19) {
            return fare.sub(new Fare(350)).discount(0.2);
        }
        return fare;
    }
}
