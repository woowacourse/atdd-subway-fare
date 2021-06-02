package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("경로 관련 기능")
class SubwayPathTest {

    @DisplayName("성인일때 거리에 따른 비용")
    @ParameterizedTest
    @CsvSource(value = {"9:1250", "12:1350", "16:1450", "51:2150"}, delimiter = ':')
    void calculateOverFareWithAdult(int distance, int targetFare) {
        //given
        SubwayPath subwayPath = new SubwayPath(new ArrayList<>(), new ArrayList<>());

        //when
        int fare = FareCalculator.calculateFare(
            distance,
            20,
            subwayPath.getSectionEdges()
        );

        //then
        assertThat(fare).isEqualTo(targetFare);
    }

    @DisplayName("청소년 일때 거리에 따른 비용")
    @ParameterizedTest
    @CsvSource(value = {"9:1250", "12:1350", "16:1450", "51:2150"}, delimiter = ':')
    void calculateOverFareWithTeenager(int distance, int targetFare) {
        //given
        SubwayPath subwayPath = new SubwayPath(new ArrayList<>(), new ArrayList<>());
        int discountTargetFare = (int) (targetFare - (targetFare - 350) * 0.2);

        //when
        int fare = FareCalculator.calculateFare(
            distance,
            15,
            subwayPath.getSectionEdges()
        );

        //then
        assertThat(fare).isEqualTo(discountTargetFare);
    }

    @DisplayName("어린이 일때 거리에 따른 비용")
    @ParameterizedTest
    @CsvSource(value = {"9:1250", "12:1350", "16:1450", "51:2150"}, delimiter = ':')
    void calculateOverFareWithChild(int distance, int targetFare) {
        //given
        SubwayPath subwayPath = new SubwayPath(new ArrayList<>(), new ArrayList<>());
        int discountTargetFare = (int) (targetFare - (targetFare - 350) * 0.5);

        //when
        int fare = FareCalculator.calculateFare(
            distance,
            7,
            subwayPath.getSectionEdges()
        );

        //then
        assertThat(fare).isEqualTo(discountTargetFare);
    }
}