package wooteco.subway.path.strategy;

public interface FareStrategy {
    int DEFAULT_FARE = 1250;

    int discount(int distance, int extraLineFare);
}
