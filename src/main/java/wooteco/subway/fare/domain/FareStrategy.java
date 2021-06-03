package wooteco.subway.fare.domain;

import java.util.Arrays;
import java.util.List;
import wooteco.subway.exception.notfound.NotExistException;
import wooteco.subway.fare.domain.farebyagestrategy.DiscountFareByAgeStrategy;
import wooteco.subway.fare.domain.farebyagestrategy.DiscountFareWhenBabyStrategy;
import wooteco.subway.fare.domain.farebyagestrategy.DiscountFareWhenChildStrategy;
import wooteco.subway.fare.domain.farebyagestrategy.DiscountFareWhenTeenagerStrategy;
import wooteco.subway.fare.domain.farebydistancestrategy.AdditionalFareOver10Km;
import wooteco.subway.fare.domain.farebydistancestrategy.AdditionalFareOver50km;
import wooteco.subway.fare.domain.farebydistancestrategy.AdditionalFareStrategy;
import wooteco.subway.fare.domain.farebydistancestrategy.AdditionalFareUnder10km;

public class FareStrategy {

    private static final List<DiscountFareByAgeStrategy> DISCOUNT_FARE_BY_AGE_STRATEGIES
        = Arrays.asList(new DiscountFareWhenBabyStrategy(), new DiscountFareWhenChildStrategy(),
        new DiscountFareWhenTeenagerStrategy());

    private static final List<AdditionalFareStrategy> additionalFareStrategies = Arrays.asList(
        new AdditionalFareUnder10km(),
        new AdditionalFareOver10Km(),
        new AdditionalFareOver50km()
    );

    public static DiscountFareByAgeStrategy distinguishAgeMember(int age) {
        return DISCOUNT_FARE_BY_AGE_STRATEGIES.stream()
            .filter(discountFareByAgeStrategy -> discountFareByAgeStrategy.isInAgeRange(age))
            .findAny()
            .orElseThrow(NotExistException::new);
    }

    public static AdditionalFareStrategy distinguishAdditionalFareStrategy(int distance) {
        return additionalFareStrategies.stream()
            .filter(additionalFareStrategy -> additionalFareStrategy.isInDistanceRange(distance))
            .findAny()
            .orElseThrow(NotExistException::new);
    }
}
