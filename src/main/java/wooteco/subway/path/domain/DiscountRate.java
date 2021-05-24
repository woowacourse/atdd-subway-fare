package wooteco.subway.path.domain;

public enum DiscountRate {
    TWENTIES(19, 0, 0),
    TEENAGERS(13, 350, 0.2),
    PRESCHOOLER(6, 350, 0.5),
    BABIES(0, 0, 0);

    private final int ageBaseline;
    private final int deductionFare;
    private final double discountRate;

    DiscountRate(int ageBaseline, int deductionFare, double discountRate) {
        this.ageBaseline = ageBaseline;
        this.deductionFare = deductionFare;
        this.discountRate = discountRate;
    }

    public static DiscountRate compareAgeBaseline(int age) {
        if (TWENTIES.ageBaseline <= age) {
            return TWENTIES;
        }
        if (TEENAGERS.ageBaseline <= age) {
            return TEENAGERS;
        }
        if (PRESCHOOLER.ageBaseline <= age) {
            return PRESCHOOLER;
        }
        return BABIES;
    }

    public int calculateFare(int currentFare) {
        return (int) ((currentFare - deductionFare) * (1 - discountRate));
    }
}
