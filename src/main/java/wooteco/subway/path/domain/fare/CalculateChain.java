package wooteco.subway.path.domain.fare;

public interface CalculateChain {
    void setNextChain(CalculateChain calculateChain);

    int calculate(int distance);
}
