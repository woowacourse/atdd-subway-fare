package wooteco.subway.path.dto;

import wooteco.subway.path.domain.FareTable;

public class FareResponse {
    private int adult;
    private int teenager;
    private int child;
    private int baby;

    public FareResponse() {
    }

    public FareResponse(int adult, int teenager, int child, int baby) {
        this.adult = adult;
        this.teenager = teenager;
        this.child = child;
        this.baby = baby;
    }

    public static FareResponse of(FareTable fareTable) {
        return new FareResponse(fareTable.getAdult(), fareTable.getTeenager(), fareTable.getChild(), fareTable.getBaby());
    }

    public int getAdult() {
        return adult;
    }

    public int getTeenager() {
        return teenager;
    }

    public int getChild() {
        return child;
    }

    public int getBaby() {
        return baby;
    }
}
