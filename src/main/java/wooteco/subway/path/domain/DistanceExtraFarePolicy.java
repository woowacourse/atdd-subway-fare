package wooteco.subway.path.domain;

import wooteco.subway.path.application.InvalidPathException;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DistanceExtraFarePolicy {

    DEFAULT(distance -> distance <= 10, 1, 0),
    UNDER_50(distance -> distance > 10 && distance <= 50, 5, 100),
    OVER_50(distance -> distance > 50, 8, 100);

    Predicate<Integer> distancePredicate;
    int delimiter;
    int additionalFare;

    DistanceExtraFarePolicy(Predicate<Integer> distancePredicate, int delimiter, int additionalFare) {
        this.distancePredicate = distancePredicate;
        this.delimiter = delimiter;
        this.additionalFare = additionalFare;
    }

    public static int apply(int distance) {
        return Arrays.stream(values())
                     .filter(it -> it.distancePredicate.test(distance))
                     .map(it -> (int) Math.ceil((distance - 10) / it.delimiter) * it.additionalFare)
                     .findAny()
                     .orElseThrow(() -> new InvalidPathException("경로당 추가요금을 계산할 수 없습니다."));
    }
}


