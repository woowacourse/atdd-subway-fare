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

    public void make(int distance, List<SectionEdge> sectionEdges) {
        Fare fare = new Fare();
        int fareByDistanceAndLine = fare.calculateByDistanceAndLine(distance, sectionEdges);
        for (DiscountPolicy discountPolicy : DiscountPolicy.values()) {
            int fareByAgeType = (int) ((fareByDistanceAndLine - discountPolicy.getDeductionFare()) * discountPolicy.getDiscountRate());
            fareTable.put(discountPolicy.getKorean(), fareByAgeType);
        }
    }

    public int calculateByAgeType(String ageType) {
        return fareTable.get(ageType);
    }
}
