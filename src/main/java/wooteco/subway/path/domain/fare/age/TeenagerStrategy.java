package wooteco.subway.path.domain.fare.age;

public class TeenagerStrategy implements AgeStrategy {
    public static final int DEDUCTED_FARE = 350;
    public static final double DISCOUNT_PERCENTAGE = 0.8;

    @Override
    public int calculate(int fare) {
        return (int) ((fare - DEDUCTED_FARE) * DISCOUNT_PERCENTAGE);
    }
}
