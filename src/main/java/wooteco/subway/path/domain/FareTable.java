package wooteco.subway.path.domain;

import wooteco.subway.path.domain.chain.FareChain;
import wooteco.subway.path.domain.chain.FirstBoundChain;
import wooteco.subway.path.domain.chain.SecondBoundChain;
import wooteco.subway.path.domain.chain.ThirdBoundChain;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FareTable {
    private final Map<String, Integer> fareTable;
    private final FareChain fareChain;

    public FareTable() {
        this.fareTable = new LinkedHashMap<>();
        this.fareChain = new FirstBoundChain(new SecondBoundChain(new ThirdBoundChain()));
    }

    public Map<String, Integer> getFareTable() {
        return Collections.unmodifiableMap(fareTable);
    }

    public void calculateFare(int distance, List<SectionEdge> sectionEdges) {
        int fareByDistanceAndLine = calculateFareByDistanceAndLine(distance, sectionEdges);
        for (DiscountPolicy discountPolicy : DiscountPolicy.values()) {
            int fare = (int) ((fareByDistanceAndLine - discountPolicy.getDeductionFare()) * discountPolicy.getDiscountRate());
            fareTable.put(discountPolicy.getKorean(), fare);
        }
    }

    private int calculateFareByDistanceAndLine(int distance, List<SectionEdge> sectionEdges) {
        int fare = fareChain.calculate(distance);
        fare += calculateAdditionalFareByLine(sectionEdges);

        return fare;
    }

    private int calculateAdditionalFareByLine(List<SectionEdge> sectionEdges) {
        return sectionEdges.stream()
                .mapToInt(sectionEdge -> sectionEdge.getLine().getExtraFare())
                .max()
                .orElse(0);
    }

    public int calculateByAgeType(String ageType) {
        return fareTable.get(ageType);
    }
}
