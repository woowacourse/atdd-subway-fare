package wooteco.subway.path.dto;

import java.util.Map;

public class FareTableResponse {
    private Map<String, Integer> fareTable;

    public FareTableResponse() {
    }

    public FareTableResponse(Map<String, Integer> fareTable) {
        this.fareTable = fareTable;
    }

    public Map<String, Integer> getFareTable() {
        return fareTable;
    }
}
