package wooteco.subway.path.domain.policy;

public interface DistancePolicy {

    void setNextPolicy(DistancePolicy nextDistancePolicy);

    int calculateFare(int distance);
}
