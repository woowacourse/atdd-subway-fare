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
    void calculateOverFareWithAdult(String distance, String targetFare) {
        //given
        SubwayPath subwayPath = new SubwayPath(new ArrayList<>(), new ArrayList<>());

        //when
        int fare = subwayPath.calculateFare(Integer.parseInt(distance), 20);

        //then
        assertThat(fare).isEqualTo(Integer.parseInt(targetFare));
    }

    @DisplayName("청소년 일때 거리에 따른 비용")
    @ParameterizedTest
    @CsvSource(value = {"9:1250", "12:1350", "16:1450", "51:2150"}, delimiter = ':')
    void calculateOverFareWithTeenager(String distance, String targetFare) {
        //given
        SubwayPath subwayPath = new SubwayPath(new ArrayList<>(), new ArrayList<>());
        int discountTargetFare = Integer.parseInt(targetFare);
        discountTargetFare -= (discountTargetFare - 350) * 0.2;

        //when
        int fare = subwayPath.calculateFare(Integer.parseInt(distance), 15);

        //then
        assertThat(fare).isEqualTo(discountTargetFare);
    }

    @DisplayName("어린이 일때 거리에 따른 비용")
    @ParameterizedTest
    @CsvSource(value = {"9:1250", "12:1350", "16:1450", "51:2150"}, delimiter = ':')
    void calculateOverFareWithChild(String distance, String targetFare) {
        //given
        SubwayPath subwayPath = new SubwayPath(new ArrayList<>(), new ArrayList<>());
        int discountTargetFare = Integer.parseInt(targetFare);
        discountTargetFare -= (discountTargetFare - 350) * 0.5;

        //when
        int fare = subwayPath.calculateFare(Integer.parseInt(distance), 7);

        //then
        assertThat(fare).isEqualTo(discountTargetFare);
    }
}