package wooteco.subway.path.domain;

import wooteco.subway.member.domain.Member;

import java.util.Objects;

public class Fare {
    private final int fare;

    public Fare(int fare) {
        this.fare = fare;
    }

    public Fare add(Fare that) {
        return new Fare(this.fare + that.fare);
    }

    public static Fare createDefaultFare(int distance) {
        FarePolicy distanceFarePolicy = FarePolicy.of(distance);
        return distanceFarePolicy.calculateFare(distance);
    }

    public Fare applyMemberDiscount(Member member) {
        return new Fare(member.calculateFare(fare));
    }

    public int getFare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fare)) return false;
        Fare fare1 = (Fare) o;
        return getFare() == fare1.getFare();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFare());
    }
}
