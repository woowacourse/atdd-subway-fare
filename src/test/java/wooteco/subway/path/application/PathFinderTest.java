package wooteco.subway.path.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.TestDataLoader;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


class PathFinderTest {

    private static final TestDataLoader testDataLoader = new TestDataLoader();
    private final PathFinder pathFinder = new PathFinder();

    private static Stream<Arguments> calculateFareWithDistance() {
        return Stream.of(
            Arguments.of(testDataLoader.강남역(), testDataLoader.정자역(), 1250),
            Arguments.of(testDataLoader.강남역(), testDataLoader.잠실역(), 2050),
            Arguments.of(testDataLoader.산본역(), testDataLoader.범계역(), 2750)
        );
    }

    @DisplayName("거리별로 요금이 계산된다")
    @ParameterizedTest
    @MethodSource("calculateFareWithDistance")
    public void calculateFareWithDistance(Station source, Station target, int expectedFare) {
        // given
        List<Line> lines = Arrays.asList(testDataLoader.신분당선(), testDataLoader.이호선(), testDataLoader.사호선());

        // when
        SubwayPath path = pathFinder.findPath(lines, source, target);
        int fare = path.calculateFare();

        // then
        assertThat(fare).isEqualTo(expectedFare);
    }
}