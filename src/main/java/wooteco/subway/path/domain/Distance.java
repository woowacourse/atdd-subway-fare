package wooteco.subway.path.domain;

import java.util.Objects;

public class Distance {
    private static final int FIRST_DISTANCE_CHARGE_THRESHOLD = 10;
    private static final int SECOND_DISTANCE_CHARGE_THRESHOLD = 50;
    private static final int FIRST_CHARGE_DISTANCE = 5;
    private static final int SECOND_CHARGE_DISTANCE = 8;
    private static final int CHARGE_AMOUNT = 100;
    private static final int FIRST_DISTANCE_FULL_CHARGE = 800;

    private final int distance;

    public Distance(final int distance) {
        this.distance = distance;
    }

    public boolean isFirstOverCharge() {
        return distance > FIRST_DISTANCE_CHARGE_THRESHOLD && distance <= SECOND_DISTANCE_CHARGE_THRESHOLD;
    }

    public boolean isSecondOverCharge() {
        return SECOND_DISTANCE_CHARGE_THRESHOLD < distance;
    }

    public boolean isNoOverCharge() {
        return distance <= FIRST_DISTANCE_CHARGE_THRESHOLD;
    }

    public int calculateFirstOverCharge() {
        return (int) ((Math.ceil((distance - FIRST_DISTANCE_CHARGE_THRESHOLD - 1) / FIRST_CHARGE_DISTANCE) + 1) * CHARGE_AMOUNT);
    }

    public int calculateSecondOverCharge() {
        return FIRST_DISTANCE_FULL_CHARGE + (int) ((Math.ceil((distance - SECOND_DISTANCE_CHARGE_THRESHOLD - 1) / SECOND_CHARGE_DISTANCE) + 1) * CHARGE_AMOUNT);
    }

    public Distance add(final Distance that) {
        return new Distance(this.distance + that.distance);
    }

    public boolean isShorterOrEqualTo(final Distance that) {
        return this.distance <= that.distance;
    }

    public Distance subtract(final Distance that) {
        return new Distance(this.distance - that.distance);
    }

    public int getDistance() {
        return distance;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
