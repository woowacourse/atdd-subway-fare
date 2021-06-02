package wooteco.subway.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.domain.Price;
import wooteco.subway.station.domain.Station;

public class PriceTest {

    @DisplayName("기본 운임인 경우")
    @Test
    void defaultPrice() {
        Price price = new Price(0);
        price.calculatePrice(10);
        assertThat(price.getPrice()).isEqualTo(1250);
    }

    @DisplayName("50키로 이하인 경우")
    @Test
    void middlePrice() {
        Price price = new Price(0);
        price.calculatePrice(50);
        assertThat(price.getPrice()).isEqualTo(2050);
    }

    @DisplayName("49키로인 경우")
    @Test
    void middlePrice49() {
        Price price = new Price(0);
        price.calculatePrice(49);
        assertThat(price.getPrice()).isEqualTo(2050);
    }

    @DisplayName("환승이 없는 경우에 요금이 추가되는 경우")
    @Test
    void oneLineExtraPrice() {
        Section section = new Section(1L, new Station(), new Station(), 10);
        Sections sections = new Sections(Collections.singletonList(section));
        new Line(1L, "testLine1", "black", sections, 900);
    }
}
