package wooteco.subway.path.domain;

import wooteco.subway.member.domain.LoginMember;

import java.util.List;
import java.util.Objects;

public class Fare {
    private static final int DEFAULT_FARE_AMOUNT = 1250;
    public static final int MINIMUM_POSSIBLE_FARE = 0;

    private final int fare;

    public Fare(final int fare) {
        if (fare < MINIMUM_POSSIBLE_FARE) {
            throw new IllegalArgumentException();
        }
        this.fare = fare;
    }


    public int getFare() {
        return fare;
    }

    public static Fare calculateTotalFare(final List<SectionEdge> sectionEdges, final Distance distance) {
        int lineExtraFare = getLineExtraFare(sectionEdges);
        int distanceCharge = DistanceCharge.getDistanceCharge(distance);

        return new Fare(DEFAULT_FARE_AMOUNT + lineExtraFare + distanceCharge);
    }

    private static int getLineExtraFare(final List<SectionEdge> sectionEdges) {
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
