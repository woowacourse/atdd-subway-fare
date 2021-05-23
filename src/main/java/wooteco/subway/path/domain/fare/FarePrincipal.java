package wooteco.subway.path.domain.fare;

import wooteco.subway.member.domain.LoginMember;

import java.util.List;

import static wooteco.subway.path.application.FarePrincipalFinder.FARE_PER_KM;

public interface FarePrincipal {
    double calculateFare(int distance, long extraFare);

    default double calculateOverFare (double baseFare, int distance, int perDistance){
        return baseFare + (Math.ceil(distance / perDistance) * FARE_PER_KM);
    }
}
