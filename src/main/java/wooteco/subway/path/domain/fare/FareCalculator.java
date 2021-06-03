package wooteco.subway.path.domain.fare;

import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.fare.ageStrategy.AgeAppliedRule;
import wooteco.subway.path.domain.fare.fareStrategy.DistanceAppliedRule;

public class FareCalculator {

    public FareCalculator() {
    }

    public Fare fare(int distance, LoginMember member, int extraFare) {
        int basicFare = calculateFareByDistance(distance) + extraFare;
        AgeAppliedRule ageAppliedRule = AgeAppliedRule.matchRule(member.getAge());
        int fare =  (int) ((basicFare - ageAppliedRule.getDeductedFare()) * ageAppliedRule
            .getAppliedRate());
        return new Fare(fare);
    }

    public int calculateFareByDistance(int distance) {
        long rawFareByDistance = DistanceAppliedRule.calculateDistanceFare(distance);
        return (int) (Math.ceil(rawFareByDistance));
    }
}
