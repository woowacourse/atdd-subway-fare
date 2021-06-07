package wooteco.subway.fare.domain.farebydistancestrategy;

import java.util.Arrays;
import java.util.List;
import wooteco.subway.exception.notfound.NotExistException;

public class FareByDistance {

    private static final List<AdditionalFareStrategy> additionalFareStrategies = Arrays.asList(
        new AdditionalFareUnder10km(),
        new AdditionalFareOver10Km(),
        new AdditionalFareOver50km()
    );

    private final int distance;

    public FareByDistance(int distance) {
        this.distance = distance;
    }

    public int calculateAdditionalFare() {
        return distinguishAdditionalFareStrategy().calculateAdditionalFare(distance);
    }

    private AdditionalFareStrategy distinguishAdditionalFareStrategy() {
        return additionalFareStrategies.stream()
            .filter(additionalFareStrategy -> additionalFareStrategy.isInDistanceRange(distance))
            .findAny()
            .orElseThrow(NotExistException::new);
    }


}
