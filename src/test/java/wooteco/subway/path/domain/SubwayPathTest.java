package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("경로 관련 기능")
class SubwayPathTest {


    @DisplayName("테스트 이름")
    @ParameterizedTest
    @CsvSource(value = {"9:1250", "12:1350", "16:1450", "51:2150"}, delimiter = ':')
    void calculateOverFare(String distance, String targetFare) {
        //given
        SubwayPath subwayPath = new SubwayPath(new ArrayList<>(), new ArrayList<>());
        
        //when
        int fare = subwayPath.calculateOverFare(Integer.parseInt(distance));

        //then
        assertThat(fare).isEqualTo(Integer.parseInt(targetFare));
    }
}