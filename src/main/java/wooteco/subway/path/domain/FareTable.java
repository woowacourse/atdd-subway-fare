package wooteco.subway.path.domain;

import static wooteco.subway.path.domain.DiscountPolicy.*;

public class FareTable {
    private final Fare adult;
    private final Fare teenager;
    private final Fare child;
    private final Fare baby;

    public FareTable(Fare adult, Fare teenager, Fare child, Fare baby) {
        this.adult = adult;
        this.teenager = teenager;
        this.child = child;
        this.baby = baby;
    }

    public static FareTable of(Fare fare) {
        return new FareTable(fare.discount(ADULT), fare.discount(TEENAGER), fare.discount(CHILD), fare.discount(BABY));
    }

    public int getAdult() {
        return adult.money();
    }

    public int getTeenager() {
        return teenager.money();
    }

    public int getChild() {
        return child.money();
    }

    public int getBaby() {
        return baby.money();
    }
}
