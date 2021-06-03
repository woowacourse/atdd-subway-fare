package wooteco.subway.path.domain.strategy.additional.age;

public abstract class AgeRange {
    private final int from;
    private final int to;

    protected AgeRange(int from, int to) {
        this.from = from;
        this.to = to;
    }

    public boolean match(int age) {
        return from <= age && to >= age;
    }
}
