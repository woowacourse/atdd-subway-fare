package wooteco.auth.domain;

import wooteco.subway.domain.fareCalculator.DefaultFareCalculator;
import wooteco.subway.domain.fareCalculator.FareCalculator;

public interface User {
    FareCalculator defaultCalculator = new DefaultFareCalculator();

    int calculateFee(int distance, int extraFare);
}
