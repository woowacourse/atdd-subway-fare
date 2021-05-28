package wooteco.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.exception.WrongLineNameException;

class LineTest {

    @Test
    @DisplayName("잘못된 이름으로 생성하는 테스트")
    void wrongNameTest() {
        //when
        String name = "노선이 아님";

        //then
        assertThatThrownBy(() -> new Line(name, "초록색"))
            .isInstanceOf(WrongLineNameException.class);
    }

}