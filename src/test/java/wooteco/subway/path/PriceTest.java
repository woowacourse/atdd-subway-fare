package wooteco.subway.path;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.path.domain.Price;

public class PriceTest {

    @DisplayName("기본 운임인 경우")
    @Test
    void defaultPrice(){
        Price price = new Price(0);
        price.calculatePrice(10);
        assertThat(price.getPrice()).isEqualTo(1250);
    }

    @DisplayName("50키로 이하인 경우")
    @Test
    void middlePrice(){
        Price price = new Price(0);
        price.calculatePrice(50);
        assertThat(price.getPrice()).isEqualTo(2050);
    }

    @DisplayName("49키로인 경우")
    @Test
    void middlePrice49(){
        Price price = new Price(0);
        price.calculatePrice(49);
        assertThat(price.getPrice()).isEqualTo(2050);
    }
}
