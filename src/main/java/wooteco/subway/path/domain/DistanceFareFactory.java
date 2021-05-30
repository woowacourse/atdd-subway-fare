package wooteco.subway.path.domain;

import wooteco.subway.path.domain.strategy.DistanceFarePolicy;
import wooteco.subway.path.domain.strategy.OverFiftyFare;
import wooteco.subway.path.domain.strategy.TenToFiftyFare;
import wooteco.subway.path.domain.strategy.UnderTenFare;

public class DistanceFareFactory {
    private static final int BASIC_DISTANCE = 10;
    private static final int OVER_DISTANCE = 50;

    public static DistanceFarePolicy create(int distance, int defaultFare) {
        if (distance <= BASIC_DISTANCE) {
            return new UnderTenFare(distance, defaultFare);
        }

        if (distance > OVER_DISTANCE) {
            return new OverFiftyFare(distance, defaultFare);
        }

        return new TenToFiftyFare(distance, defaultFare);
    }
}
