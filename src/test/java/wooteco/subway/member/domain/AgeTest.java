package wooteco.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class AgeTest {

    @DisplayName("나이가 1이상 200이하가 아니면 에러를 반환한다")
    @Test
    void age_outOfBounds_throwException() {
        assertThatThrownBy(() -> new Age(0)).isInstanceOf(IllegalAgeException.class);
        assertThatThrownBy(() -> new Age(201)).isInstanceOf(IllegalAgeException.class);
    }

    @DisplayName("나이가 6이상 13미만이면 true 아니면 false")
    @ParameterizedTest
    @CsvSource({"5,false","6,true","12,true","13,false"})
    void isChildren(int ageValue, boolean result) {
        Age age = new Age(ageValue);
        assertThat(age.isChildren()).isEqualTo(result);
    }

    @DisplayName("나이가 13이상 19미만이면 true 아니면 false")
    @ParameterizedTest
    @CsvSource({"12,false","13,true","18,true","19,false"})
    void isTeenager(int ageValue, boolean result) {
        Age age = new Age(ageValue);
        assertThat(age.isTeenager()).isEqualTo(result);
    }
}