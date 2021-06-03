package wooteco.subway.path.domain.policy;

public interface Policy {

    void setNextLevelPolicy(Policy nextLevelPolicy);

    int calculateTotalFare(int distance);
}
