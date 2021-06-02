package wooteco.subway.path.dto;

import wooteco.subway.path.domain.FareTable;

import java.util.HashMap;
import java.util.Map;

public class FareResponse {
    Map<String, Integer> fare;

    public FareResponse() {
    }

    public FareResponse(Map<String, Integer> fare) {
        this.fare = fare;
    }

    public static FareResponse of(FareTable fareTable) {
        Map<String, Integer> fare = new HashMap<>();
        fare.put("성인", fareTable.getAdult());
        fare.put("청소년", fareTable.getTeenager());
        fare.put("어린이", fareTable.getChild());
        fare.put("유아", fareTable.getBaby());
        return new FareResponse(fare);
    }

    public Map<String, Integer> getFare() {
        return fare;
    }
}
