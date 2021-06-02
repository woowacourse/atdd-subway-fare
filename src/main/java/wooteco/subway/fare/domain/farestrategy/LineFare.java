package wooteco.subway.fare.domain.farestrategy;

import wooteco.subway.fare.domain.Money;

import java.math.BigDecimal;

public class LineFare implements FareStrategy {
    private final int lineExtraFare;

    public LineFare(int lineExtraFare) {
        this.lineExtraFare = lineExtraFare;
    }

    @Override
    public Money calculate(Money value) {
        return value.add(lineExtraFare);
    }
}
