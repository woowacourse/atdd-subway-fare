package wooteco.subway.path.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("DistanceFarePolicy 클래스")
class DistanceFarePolicyTest {

    @Nested
    @DisplayName("distancePolicyAppliedFare 메서드는")
    class Describe_distancePolicyAppliedFare {

        @Nested
        @DisplayName("이동거리가 10km 초과 50km 이하인 경우와 이동거리가 초과 50Km 초과인 경우리면")
        class Context_with_ten_over_fifty_less_distance {

            @ParameterizedTest
            @CsvSource({
                "1250, 10, 1250",
                "1250, 11, 1350",
                "1250, 50, 2050",
                "1250, 51, 2150",
                "1250, 106, 2750",
            })
            @DisplayName("각각 기존 요금에 5km마다 추가요금 100원을 적용하고, 8km마다 추가요금 100원을 적용한 요금을 리턴한다.")
            void it_returns_applied_distance_policy_fare(int originFare, int totalDistance, int actual) {
                Assertions.assertEquals(DistanceFarePolicy.distancePolicyAppliedFare(originFare, totalDistance), actual);
            }
        }
    }

}