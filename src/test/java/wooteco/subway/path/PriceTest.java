package wooteco.subway.path;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.path.domain.Price;

public class PriceTest {


    @DisplayName("기본 운임인 경우")
    @Test
    void defaultPrice(){
        assertThat(new Price(10).getPrice()).isEqualTo(1250);
    }

    @DisplayName("50키로 이하인 경우")
    @Test
    void middlePrice(){
        assertThat(new Price(50).getPrice()).isEqualTo(2050);
    }

    @DisplayName("49키로인 경우")
    @Test
    void middlePrice49(){
        assertThat(new Price(49).getPrice()).isEqualTo(2050);
    }
}
