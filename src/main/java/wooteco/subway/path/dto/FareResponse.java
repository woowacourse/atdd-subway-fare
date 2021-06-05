package wooteco.subway.path.dto;

import wooteco.subway.path.domain.fare.FareTable;

public class FareResponse {
    private Integer adult;
    private Integer teenager;
    private Integer child;
    private Integer baby;

    public FareResponse() {
    }

    public FareResponse(FareTable fareTable) {
        this.adult = fareTable.getAdultFare();
        this.teenager = fareTable.getTeenFare();
        this.child = fareTable.getChildFare();
        this.baby = fareTable.getBabyFare();
    }

    public static FareResponse of(FareTable fareTable) {
        return new FareResponse(fareTable);
    }

    public Integer getAdult() {
        return adult;
    }

    public Integer getTeenager() {
        return teenager;
    }

    public Integer getChild() {
        return child;
    }

    public Integer getBaby() {
        return baby;
    }
}
