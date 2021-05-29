package wooteco.subway.path.domain;

import java.math.BigDecimal;
import java.util.List;
import wooteco.subway.line.domain.Line;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.domain.policy.discountpolicy.DiscountFarePolicy;
import wooteco.subway.path.domain.policy.extrafarepolicy.ExtraFarePolicy;

public class Fare {

    private final List<ExtraFarePolicy> farePolicies;
    private final List<DiscountFarePolicy> discountPolicies;

    public Fare(List<ExtraFarePolicy> farePolicies, List<DiscountFarePolicy> discountPolicies) {
        this.farePolicies = farePolicies;
        this.discountPolicies = discountPolicies;
    }

    public BigDecimal calculate(SubwayPath subwayPath, LoginMember loginMember) {
        BigDecimal fare = calculateExtraFareOfDistance(subwayPath)
            .add(calculateMaximumExtraFareOfLines(subwayPath));

        return fare.subtract(calculateDiscountPolicy(loginMember, fare));
    }

    private BigDecimal calculateExtraFareOfDistance(SubwayPath subwayPath) {
        int distance = subwayPath.calculateDistance();

        return farePolicies.stream().map(
            farePolicy -> farePolicy.calculate(distance)
        ).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateMaximumExtraFareOfLines(SubwayPath subwayPath) {
        int extraFare = subwayPath.getSectionEdges().stream()
            .map(SectionEdge::getLine)
            .mapToInt(Line::getExtraFare)
            .max()
            .orElse(0);

        return BigDecimal.valueOf(extraFare);
    }

    private BigDecimal calculateDiscountPolicy(LoginMember loginMember, BigDecimal fare) {
        return discountPolicies.stream()
            .map(farePolicy -> farePolicy.calculate(loginMember).apply(fare))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
