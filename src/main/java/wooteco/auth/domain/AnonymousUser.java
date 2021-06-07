package wooteco.auth.domain;

public class AnonymousUser implements User{

    @Override
    public int calculateFee(int distance, int extraFare) {
        return defaultCalculator.calculateFare(distance, extraFare);
    }
}
