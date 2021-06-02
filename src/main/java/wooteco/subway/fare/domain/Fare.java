package wooteco.subway.fare.domain;

import wooteco.subway.fare.domain.farestrategy.FarePolicy;

public class Fare {
    private final Money initialFare;
    private final FarePolicy farePolicy;

    public Fare(Money initialFare, FarePolicy farePolicy) {
        this.initialFare = initialFare;
        this.farePolicy = farePolicy;
    }

    public int calculate() {
        return farePolicy.calculateTotalFare(initialFare).toInt();
    }
}
