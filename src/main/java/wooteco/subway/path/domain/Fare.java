package wooteco.subway.path.domain;

import java.math.BigDecimal;
import java.util.List;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.policy.discountpolicy.DiscountPolicy;
import wooteco.subway.path.domain.policy.extrafarepolicy.ExtraFarePolicy;

public class Fare {

    private final List<ExtraFarePolicy> distanceFarePolicies;
    private final List<DiscountPolicy> discountPolicies;

    public Fare(List<ExtraFarePolicy> distanceFarePolicies, List<DiscountPolicy> discountPolicies) {
        this.distanceFarePolicies = distanceFarePolicies;
        this.discountPolicies = discountPolicies;
    }

    public BigDecimal calculate(SubwayPath subwayPath, LoginMember loginMember) {
        BigDecimal fare = calculateDistanceExtraFare(subwayPath)
            .add(calculateLineExtraFare(subwayPath));

        return fare.subtract(calculateDiscountPolicy(loginMember, fare));
    }

    private BigDecimal calculateDistanceExtraFare(SubwayPath subwayPath) {
        int distance = subwayPath.calculateDistance();

        return distanceFarePolicies.stream()
            .filter(extraFarePolicy -> extraFarePolicy.isSatisfied(distance))
            .map(extraFarePolicy -> extraFarePolicy.calculate(distance))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateLineExtraFare(SubwayPath subwayPath) {
        int extraFare = subwayPath.getSectionEdges().stream()
            .map(SectionEdge::getLine)
            .mapToInt(Line::getExtraFare)
            .max()
            .orElse(0);

        return BigDecimal.valueOf(extraFare);
    }

    private BigDecimal calculateDiscountPolicy(LoginMember loginMember, BigDecimal currentFare) {
        return discountPolicies.stream()
            .filter(discountPolicy -> discountPolicy.isSatisfied(loginMember))
            .map(discountPolicy -> discountPolicy.calculate(loginMember, currentFare))
            .findAny()
            .orElse(BigDecimal.ZERO);
    }

}
