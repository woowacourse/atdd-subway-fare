package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class FareAdjusterByAgeTest {

    @ParameterizedTest
    @CsvSource({"1,1250,100,0", "6,1250,200,550", "14,1250,300,960", "20,1250,400,1650"})
    @DisplayName("나이별 할인 정책을 적용한 요금을 구한다.")
    void of(int age, int fare, int lineFare, int expected) {
        int result = FareAdjusterByAge.of(age, fare, lineFare);

        assertThat(result).isEqualTo(expected);
    }
}