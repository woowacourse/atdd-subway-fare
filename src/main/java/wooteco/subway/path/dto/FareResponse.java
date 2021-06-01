package wooteco.subway.path.dto;

import wooteco.subway.path.domain.fare.FareTable;

public class FareResponse {
    private int adult;
    private int teenager;
    private int child;
    private int baby;

    public FareResponse(int adult, int teenager, int child, int baby) {
        this.adult = adult;
        this.teenager = teenager;
        this.child = child;
        this.baby = baby;
    }

    public static FareResponse of(FareTable fare) {
        return new FareResponse(fare.getAdultFare(), fare.getTeenager(), fare.getChild(),fare.getBaby());
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
