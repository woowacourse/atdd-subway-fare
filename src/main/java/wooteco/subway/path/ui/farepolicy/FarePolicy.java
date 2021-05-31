package wooteco.subway.path.ui.farepolicy;

public interface FarePolicy {
    int deductible = 350;

    int discount(int fare);
}
