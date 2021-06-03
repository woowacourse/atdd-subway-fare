package wooteco.subway.path.domain.fare;

public class FarePrincipalFinder {
    public static final int BASIC_FARE = 1250;
    public static final int OVER_FARE = 2050;
    public static final double FARE_PER_KM = 100;
    public static final int FIRST_OVER_FARE_DISTANCE = 10;
    public static final int SECOND_OVER_FARE_DISTANCE = 50;

    private FarePrincipalFinder() {
    }

    public static FarePrincipal findFarePrincipal(Integer age) {
        if (age == null) {
            return new NoneLoginFarePrincipal();
        }

        if (age < 6) {
            return new ToddlerFarePrincipal();
        }

        if (age < 13) {
            return new KidFarePrincipal();
        }

        if (age < 19) {
            return new TeenagerFarePrincipal();
        }
        return new AdultFarePrincipal();
    }
}
