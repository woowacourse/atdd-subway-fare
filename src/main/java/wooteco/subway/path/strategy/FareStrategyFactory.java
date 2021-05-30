package wooteco.subway.path.strategy;

import java.util.ArrayList;
import java.util.List;

public class FareStrategyFactory {
    private static final List<FareStrategy> fareStrategies = initStrategy();

    private FareStrategyFactory() {
    }

    private static List<FareStrategy> initStrategy() {
        List<FareStrategy> fareStrategies = new ArrayList<>();

        fareStrategies.add(new AdultFareStrategy());
        fareStrategies.add(new ChildFareStrategy());
        fareStrategies.add(new PreSchoolFareStrategy());
        fareStrategies.add(new TeenagerFareStrategy());

        return fareStrategies;
    }

    public static FareStrategy findStrategy(int age) {
        return fareStrategies.stream()
                .filter(strategy -> strategy.isAppropriate(age))
                .findFirst()
                .get();

    }
}
