package wooteco.subway.section.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.InvalidIdException;

class IdTest {

    @Test
    @DisplayName("생성자에 NULL이 들어왔을 경우 예외처리")
    void createNullException() {
        // given

        // when

        // then
        assertThatThrownBy(() -> new Id(null))
            .isInstanceOf(InvalidIdException.class);
    }
}