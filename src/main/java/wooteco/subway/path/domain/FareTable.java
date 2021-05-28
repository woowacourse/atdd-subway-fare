package wooteco.subway.path.domain;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FareTable {
    private final Map<String, Integer> fareTable;

    public FareTable() {
        this.fareTable = new LinkedHashMap<>();
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
        int fare = 1_250;

        fare += calculateAdditionalFareByDistance(distance);
        fare += calculateAdditionalFareByLine(sectionEdges);

        return fare;
    }

    private int calculateAdditionalFareByDistance(int distance) {
        if (distance <= 10) {
            return 0;
        }
        if (distance <= 50) {
            return calculateAdditionalFareByDistanceUnderFifty(distance);
        }
        return calculateAdditionalFareByDistanceOverFifty(distance);
    }

    private int calculateAdditionalFareByDistanceUnderFifty(int distance) {
        return calculateOverFare(distance - 10, 5);
    }

    private int calculateAdditionalFareByDistanceOverFifty(int distance) {
        return calculateAdditionalFareByDistanceUnderFifty(distance - (distance - 50)) + calculateOverFare(distance - 50, 8);
    }

    private int calculateOverFare(int distance, int overDistance) {
        return (int) ((Math.ceil((distance - 1) / overDistance) + 1) * 100);
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
