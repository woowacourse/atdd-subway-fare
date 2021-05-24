package wooteco.subway.path.domain;

public class DefaultFareDistance implements FareDistance {

    @Override
    public Fare getFare(Fare currentFare) {
        return currentFare;
    }
}
