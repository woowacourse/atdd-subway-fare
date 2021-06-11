package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.InvalidFareException;

public class FareTest {

    @Test
    @DisplayName("요금 음수 생성 예외처리 테스트")
    void validateNegative() {
        // given

        // when

        // then
        assertThat(new Fare(10)).isInstanceOf(Fare.class);
        assertThatThrownBy(() -> new Fare(-1)).isInstanceOf(InvalidFareException.class);
    }
}
