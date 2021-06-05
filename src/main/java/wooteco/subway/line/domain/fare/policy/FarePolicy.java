package wooteco.subway.line.domain.fare.policy;

public interface FarePolicy {
    int deductible = 350;

    int discount(int fare);
}
