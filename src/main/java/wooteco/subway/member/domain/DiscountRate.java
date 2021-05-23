package wooteco.subway.member.domain;

public enum DiscountRate {
    ADULT(0),
    TEENAGE(0.2),
    KID(0.5);

    private final double rate;

    DiscountRate(final double rate) {
        this.rate = rate;
    }

    public static DiscountRate rate(final int age) {
        if (age >= 6 && age < 13) {
            return KID;
        }
        if (age >= 13 && age < 19) {
            return TEENAGE;
        }
        return ADULT;
    }

    public double getRate() {
        return rate;
    }
}
