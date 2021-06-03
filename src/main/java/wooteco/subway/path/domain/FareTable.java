package wooteco.subway.path.domain;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class FareTable {
    private static final int DEFAULT_FARE = 1_250;

    private final Map<String, Integer> fareTable;

    public FareTable() {
        this.fareTable = new LinkedHashMap<>();
    }

    public void calculateFare(List<SectionEdge> sectionEdges, int distance) {
        int fareWithDistanceAndLine = calculateFareByDistanceAndLine(sectionEdges, distance);
        for (DiscountPolicy discountPolicy : DiscountPolicy.values()) {
            int totalFare = (int) ((fareWithDistanceAndLine - discountPolicy.getDefaultDeduction()) * discountPolicy.getDiscountRate());
            fareTable.put(discountPolicy.getKorean(), totalFare);
        }
    }

    private int calculateFareByDistanceAndLine(List<SectionEdge> sectionEdges, int distance) {
        int fare = DEFAULT_FARE;

        fare += calculateAdditionalFareByDistance(distance);
        fare += calculateAdditionalLineFare(sectionEdges);

        return fare;
    }

    private int calculateAdditionalLineFare(List<SectionEdge> sectionEdges) {
        return sectionEdges
            .stream()
            .mapToInt(SectionEdge::getLineExtraFare)
            .max()
            .orElse(0);
    }

    private int calculateAdditionalFareByDistance(int distance) {
        int fare = 0;

        if (distance > 10 && distance <= 50) {
            fare += calculateAdditionalFareByDistanceUnderFifty(distance);
        }

        if (distance > 50) {
            fare += calculateAdditionalFareByDistanceOverFifty(distance);
        }
        return fare;
    }

    private int calculateAdditionalFareByDistanceUnderFifty(int distance) {
        return calculateOverFareByKM(distance - 10, 5);
    }

    private int calculateAdditionalFareByDistanceOverFifty(int distance) {
        return calculateAdditionalFareByDistanceUnderFifty(distance - (distance - 50)) + calculateOverFareByKM(distance - 50, 8);
    }

    private int calculateOverFareByKM(int distance, int km) {
        return (int) ((Math.ceil((distance - 1) / km) + 1) * 100);
    }

    public Map<String, Integer> getFareTable() {
        return fareTable;
    }

    public int findByAge(String age) {
        return fareTable.get(age);
    }
}
