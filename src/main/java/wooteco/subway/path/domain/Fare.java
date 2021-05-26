package wooteco.subway.path.domain;

import java.math.BigDecimal;
import java.util.List;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.farepolicy.FarePolicy;

public class Fare {

    private final List<FarePolicy> farePolicies;

    public Fare(List<FarePolicy> farePolicies) {
        this.farePolicies = farePolicies;
    }

    public BigDecimal calculate(SubwayPath subwayPath) {
        return calculateExtraFareOfDistance(subwayPath)
            .add(calculateMaximumExtraFareOfLines(subwayPath));
    }

    private BigDecimal calculateExtraFareOfDistance(SubwayPath subwayPath) {
        int distance = subwayPath.calculateDistance();

        return farePolicies.stream().map(
            farePolicy -> farePolicy.calculate(distance)
        ).reduce(BigDecimal.valueOf(0), BigDecimal::add);
    }

    private BigDecimal calculateMaximumExtraFareOfLines(SubwayPath subwayPath) {
        int extraFare = subwayPath.getSectionEdges().stream()
            .map(SectionEdge::getLine)
            .mapToInt(Line::getExtraFare)
            .max()
            .orElse(0);

        return BigDecimal.valueOf(extraFare);
    }

}
