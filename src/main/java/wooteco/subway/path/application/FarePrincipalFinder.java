package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.path.domain.fare.*;

@Service
public class FarePrincipalFinder {
    public static final int BASIC_FARE = 1250;
    public static final int OVER_FARE = 2050;
    public static final double FARE_PER_KM = 100;
    public static final int FIRST_OVER_FARE_DISTANCE = 10;
    public static final int SECOND_OVER_FARE_DISTANCE = 50;

    public FarePrincipal findFarePrincipal(Integer age) {
        if (age == null) {
            return new NoneLoginFarePrincipal();
        }

        if (age >= 6 && age < 13) {
            return new KidsFarePrincipal();
        }

        if (age >= 13 && age < 19) {
            return new TeenagerFarePrincipal();
        }
        return new AdultFarePrincipal();
    }
}
