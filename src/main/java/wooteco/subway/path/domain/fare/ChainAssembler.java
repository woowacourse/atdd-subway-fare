package wooteco.subway.path.domain.fare;

public class ChainAssembler {
    private final CalculateChain first;

    public ChainAssembler() {
        first = new FirstChain();
        CalculateChain second = new SecondChain();
        CalculateChain third = new ThirdChain();

        first.setNextChain(second);
        second.setNextChain(third);
    }

    public int calculateByDistance(int distance) {
        return first.calculate(distance);
    }
}
