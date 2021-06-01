package wooteco.subway.path.domain.fare;

public interface FareChain {
    void setNextChainByChain(FareChain fareChain);
    int calculateFare(int distance);
}
