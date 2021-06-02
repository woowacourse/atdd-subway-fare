package wooteco.subway.path.domain.fare;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.path.domain.SubwayPath;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class PathCalculatorTest {

    @Mock
    private SubwayPath subwayPath;

    @ParameterizedTest
    @ValueSource(ints = {100, 200, 500})
    @DisplayName("노선별 추가요금이 있으면, 추가된다. ")
    void lineExtraFareTest(final int lineExtraFare) {
        int age = 20;
        int distance = 10;
        int expectedFare = FarePolicy.DEFAULT_FARE.getFare() + lineExtraFare;

        given(subwayPath.getDistance()).willReturn(distance);
        given(subwayPath.getMaximumExtraFareByLine()).willReturn(lineExtraFare);

        assertThat(FareCalculator.calculateFare(subwayPath, age)).isEqualTo(expectedFare);
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 5, 10})
    @DisplayName("이동 거리가 10km 이하이면, 거리별 추가요금이 붙지 않는다.")
    void distanceTest(final int distance) {
        int age = 20;
        int lineExtraFare = 0;
        int expectedFare = FarePolicy.DEFAULT_FARE.getFare() + lineExtraFare;

        given(subwayPath.getDistance()).willReturn(distance);
        given(subwayPath.getMaximumExtraFareByLine()).willReturn(lineExtraFare);

        assertThat(FareCalculator.calculateFare(subwayPath, age)).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @CsvSource({"12,1350", "16,1450", "50,2050"})
    @DisplayName("이동 거리가 10km 초과하고 50km 이하이면, 5km 별로 초과요금이 붙는다.")
    void additionalDistanceTest(final int distance, final int expectedFare) {
        int age = 20;
        int lineExtraFare = 0;

        given(subwayPath.getDistance()).willReturn(distance);
        given(subwayPath.getMaximumExtraFareByLine()).willReturn(lineExtraFare);

        assertThat(FareCalculator.calculateFare(subwayPath, age)).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @CsvSource({"51,2150", "57,2150", "58,2150", "59,2250"})
    @DisplayName("이동 거리가 50km 초과하면, 51km 이상부터는 8km 별로 초과요금이 붙는다.")
    void additionalLongDistanceTest(final int distance, final int expectedFare) {
        int age = 20;
        int lineExtraFare = 0;

        given(subwayPath.getDistance()).willReturn(distance);
        given(subwayPath.getMaximumExtraFareByLine()).willReturn(lineExtraFare);

        assertThat(FareCalculator.calculateFare(subwayPath, age)).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 5})
    @DisplayName("아기이면, 요금을 받지 않는다. ")
    void babyAgeTest(final int age) {
        int lineExtraFare = 0;
        int distance = 10;
        int expectedFare = 0;

        given(subwayPath.getDistance()).willReturn(distance);
        given(subwayPath.getMaximumExtraFareByLine()).willReturn(lineExtraFare);

        assertThat(FareCalculator.calculateFare(subwayPath, age)).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @ValueSource(ints = {6, 7, 8, 9, 10, 11, 12})
    @DisplayName("나이가 어린이(6살~12살)이면, (기본요금 + 500원 - 미성년자할인(350원) )에서 50퍼 할인 금액이 나온다. ")
    void childAgeTest(final int age) {
        int lineExtraFare = 0;
        int distance = 10;
        int expectedFare = (int)((FarePolicy.DEFAULT_FARE.getFare() - FarePolicy.MINOR_DISCOUNT_FARE.getFare() + lineExtraFare) * 0.5);

        given(subwayPath.getDistance()).willReturn(distance);
        given(subwayPath.getMaximumExtraFareByLine()).willReturn(lineExtraFare);

        assertThat(FareCalculator.calculateFare(subwayPath, age)).isEqualTo(expectedFare);
    }

    @ParameterizedTest
    @ValueSource(ints = {13, 14, 15, 16, 17, 18, 18})
    @DisplayName("나이가 청소년(13살~18살)이면, (기본요금 + 500원 - 미성년자할인(350원) )에서 80퍼 할인 금액이 나온다. ")
    void teenAgeTest(final int age) {
        int lineExtraFare = 0;
        int distance = 10;
        int expectedFare = (int)((FarePolicy.DEFAULT_FARE.getFare() - FarePolicy.MINOR_DISCOUNT_FARE.getFare() + lineExtraFare) * 0.8);

        given(subwayPath.getDistance()).willReturn(distance);
        given(subwayPath.getMaximumExtraFareByLine()).willReturn(lineExtraFare);

        assertThat(FareCalculator.calculateFare(subwayPath, age)).isEqualTo(expectedFare);
    }
}
