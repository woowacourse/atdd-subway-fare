package wooteco.subway.domain.fareCalculator;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FareCalculatorTest {

    private FareCalculator fareCalculator = new DefaultFareCalculator();

    @Test
    @DisplayName("10km 미만일 때(기본요금)")
    public void underTen() throws Exception {
        int fare = fareCalculator.calculateFare(9, 0);
        assertThat(fare).isEqualTo(1250);
    }

    @Test
    @DisplayName("10km~50km일 때")
    public void betweenTenFifty() throws Exception {
        int fare = fareCalculator.calculateFare(11, 0);
        assertThat(fare).isEqualTo(1350);

        fare = fareCalculator.calculateFare(16, 0);
        assertThat(fare).isEqualTo(1450);

        fare = fareCalculator.calculateFare(49, 0);
        assertThat(fare).isEqualTo(2050);

        fare = fareCalculator.calculateFare(50, 0);
        assertThat(fare).isEqualTo(2050);
    }

    @Test
    @DisplayName("50km 초과일 시")
    public void overFifty() throws Exception {
        int fare = fareCalculator.calculateFare(51, 0);
        assertThat(fare).isEqualTo(2150);

        fare = fareCalculator.calculateFare(59, 0);
        assertThat(fare).isEqualTo(2250);
    }

    @Nested
    @DisplayName("나이별 정책 할인 포함시 (BABY)")
    class AgeDiscountBaby {

        @BeforeEach
        void setUp() {
            fareCalculator = new AgeDiscountFareCalculator(fareCalculator, 2);
        }

        @Test
        @DisplayName("모두 공짜인지")
        public void baby_free() throws Exception{
            int fare = fareCalculator.calculateFare(10, 0);
            assertThat(fare).isEqualTo(0);

            fare = fareCalculator.calculateFare(50, 10);
            assertThat(fare).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("나이별 정책 할인 포함시 (CHILDREN)")
    class AgeDiscountChildren {

        @BeforeEach
        void setUp() {
            fareCalculator = new AgeDiscountFareCalculator(fareCalculator, 7);
        }

        @Test
        @DisplayName("10km 미만일 때(기본요금)")
        public void underTen() throws Exception {
            int fare = fareCalculator.calculateFare(9, 0);
            assertThat(fare).isEqualTo(450);
        }

        @Test
        @DisplayName("10km~50km일 때")
        public void betweenTenFifty() throws Exception {
            int fare = fareCalculator.calculateFare(11, 0);
            assertThat(fare).isEqualTo(500);

            fare = fareCalculator.calculateFare(16, 0);
            assertThat(fare).isEqualTo(550);

            fare = fareCalculator.calculateFare(49, 0);
            assertThat(fare).isEqualTo(850);

            fare = fareCalculator.calculateFare(50, 0);
            assertThat(fare).isEqualTo(850);
        }

        @Test
        @DisplayName("50km 초과일 시")
        public void overFifty() throws Exception {
            int fare = fareCalculator.calculateFare(51, 0);
            assertThat(fare).isEqualTo(900);

            fare = fareCalculator.calculateFare(59, 0);
            assertThat(fare).isEqualTo(950);
        }
    }

    @Nested
    @DisplayName("나이별 정책 할인 포함시 (TEENAGER)")
    class AgeDiscountTeenager {

        @BeforeEach
        void setUp() {
            fareCalculator = new AgeDiscountFareCalculator(fareCalculator, 14);
        }

        @Test
        @DisplayName("10km 미만일 때(기본요금)")
        public void underTen() throws Exception {
            int fare = fareCalculator.calculateFare(9, 0);
            assertThat(fare).isEqualTo(720);
        }

        @Test
        @DisplayName("10km~50km일 때")
        public void betweenTenFifty() throws Exception {
            int fare = fareCalculator.calculateFare(11, 0);
            assertThat(fare).isEqualTo(800);

            fare = fareCalculator.calculateFare(16, 0);
            assertThat(fare).isEqualTo(880);

            fare = fareCalculator.calculateFare(49, 0);
            assertThat(fare).isEqualTo(1360);

            fare = fareCalculator.calculateFare(50, 0);
            assertThat(fare).isEqualTo(1360);
        }

        @Test
        @DisplayName("50km 초과일 시")
        public void overFifty() throws Exception {
            int fare = fareCalculator.calculateFare(51, 0);
            assertThat(fare).isEqualTo(1440);

            fare = fareCalculator.calculateFare(59, 0);
            assertThat(fare).isEqualTo(1520);
        }
    }

    @Nested
    @DisplayName("나이별 정책 할인 포함시 (ADULT)")
    class AgeDiscountAdult {

        @BeforeEach
        void setUp() {
            fareCalculator = new AgeDiscountFareCalculator(fareCalculator, 19);
        }

        @Test
        @DisplayName("10km 미만일 때(기본요금)")
        public void underTen() throws Exception {
            int fare = fareCalculator.calculateFare(9, 0);
            assertThat(fare).isEqualTo(1250);
        }

        @Test
        @DisplayName("10km~50km일 때")
        public void betweenTenFifty() throws Exception {
            int fare = fareCalculator.calculateFare(11, 0);
            assertThat(fare).isEqualTo(1350);

            fare = fareCalculator.calculateFare(16, 0);
            assertThat(fare).isEqualTo(1450);

            fare = fareCalculator.calculateFare(49, 0);
            assertThat(fare).isEqualTo(2050);

            fare = fareCalculator.calculateFare(50, 0);
            assertThat(fare).isEqualTo(2050);
        }

        @Test
        @DisplayName("50km 초과일 시")
        public void overFifty() throws Exception {
            int fare = fareCalculator.calculateFare(51, 0);
            assertThat(fare).isEqualTo(2150);

            fare = fareCalculator.calculateFare(59, 0);
            assertThat(fare).isEqualTo(2250);
        }
    }
}