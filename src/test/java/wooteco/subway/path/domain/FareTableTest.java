package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FareTableTest {

    @DisplayName("요금 테이블 생성된다.")
    @Test
    void of() {
        //given
        Fare fare = Fare.of(new Money(1250));

        //when
        FareTable fareTable = FareTable.of(fare);

        //then
        assertThat(fareTable.getAdult()).isEqualTo(1250);
        assertThat(fareTable.getTeenager()).isEqualTo(720);
        assertThat(fareTable.getChild()).isEqualTo(450);
        assertThat(fareTable.getBaby()).isEqualTo(0);
    }
}