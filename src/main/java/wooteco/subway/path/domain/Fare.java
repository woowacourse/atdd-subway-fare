package wooteco.subway.path.domain;

import wooteco.subway.member.domain.LoginMember;

import java.util.List;
import java.util.Objects;

public class Fare {
    private static final int DEFAULT_FARE_AMOUNT = 1250;
    private static Fare DEFAULT_FARE;

    private final int fare;

    public Fare(final int fare) {
        this.fare = fare;
    }

    public static Fare createDefaultFare() {
        if (DEFAULT_FARE == null) {
            DEFAULT_FARE = new Fare(DEFAULT_FARE_AMOUNT);
        }
        return DEFAULT_FARE;
    }

    public int getFare() {
        return fare;
    }

    public Fare calculateTotalFare(final List<SectionEdge> sectionEdges, final Distance distance) {
        int lineExtraFare = getLineExtraFare(sectionEdges);
        int distanceFare = DistanceCharge.getDistanceCharge(distance);

        return new Fare(this.fare + lineExtraFare + distanceFare);
    }

    private int getLineExtraFare(final List<SectionEdge> sectionEdges) {
        return sectionEdges.stream()
                .mapToInt(it -> it.getLine().getExtraFare().fare)
                .max()
                .getAsInt();
    }

    public Fare calculateFareAfterDiscount(final LoginMember loginMember) {
        if (loginMember.getId() == null) {
            return this;
        }
        int discountAmount = Discount.getDiscountAmount(loginMember.getAge(), this.fare);
        return new Fare(this.fare - discountAmount);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
