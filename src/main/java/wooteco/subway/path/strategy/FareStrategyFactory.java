package wooteco.subway.path.strategy;

public class FareStrategyFactory {
    private static final int PRESCHOOL_AGE = 6;
    private static final int CHILD_AGE = 13;
    private static final int TEENAGER_AGE = 19;

    private FareStrategyFactory() {
    }

    public static FareStrategy findStrategy(int age) {
        if (age < PRESCHOOL_AGE) {
            return new PreSchoolFareStrategy();
        }

        if (age < CHILD_AGE) {
            return new ChildFareStrategy();
        }

        if (age < TEENAGER_AGE) {
            return new TeenagerFareStrategy();
        }

        return new AdultFareStrategy();
    }
}
