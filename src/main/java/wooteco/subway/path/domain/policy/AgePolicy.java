package wooteco.subway.path.domain.policy;

public interface AgePolicy {

    void setNextPolicy(AgePolicy nextAgePolicy);

    int calculateFare(int age, int fare);
}
