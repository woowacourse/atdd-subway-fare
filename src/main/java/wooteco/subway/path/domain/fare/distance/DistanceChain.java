package wooteco.subway.path.domain.fare.distance;

public interface DistanceChain {
    void setNextChain(DistanceChain nextChain);

    int calculate(int distance);
}
