package wooteco.subway.path.domain.fare.creationstrategy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.exception.application.ValidationFailureException;

public class DistanceTypeTest {

    @ParameterizedTest
    @DisplayName("거리 타입 생성")
    @MethodSource("DistanceTypeValues")
    void createDistanceType(int distance, DistanceType expectedDistanceType) {
        //when
        DistanceType distanceType = DistanceType.from(distance);

        //then
        assertThat(distanceType).isEqualTo(expectedDistanceType);
    }

    private static Stream<Arguments> DistanceTypeValues() {
        return Stream.of(
            Arguments.of(0, DistanceType.BASIC_DISTANCE),
            Arguments.of(10, DistanceType.BASIC_DISTANCE),
            Arguments.of(11, DistanceType.SHORT_INTERVAL_DISTANCE),
            Arguments.of(50, DistanceType.SHORT_INTERVAL_DISTANCE),
            Arguments.of(51, DistanceType.LONG_INTERVAL_DISTANCE),
            Arguments.of(200, DistanceType.LONG_INTERVAL_DISTANCE)
        );
    }

    @Test
    @DisplayName("값이 음수이면 거리 타입 생성 실패")
    void createDistanceType_fail_negativeDistance() {
        // when, then
        assertThatThrownBy(() -> DistanceType.from(-1))
            .isInstanceOf(ValidationFailureException.class)
            .hasMessageContaining("거리는 음수일 수 없습니다");
    }
}
