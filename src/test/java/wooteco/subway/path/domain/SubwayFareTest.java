package wooteco.subway.path.domain;


import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


@DisplayName("SubwayFare 클래스")
class SubwayFareTest {

    @Nested
    @DisplayName("생성자는")
    class Describe_getValue {

        @Nested
        @DisplayName("지하철 경로와 성인 승객 나이가 주어지면")
        class Context_adult_fare {
            @Mock
            private SubwayPath subwayPath;

            @Test
            @DisplayName("이동 거리, 노선 추가 요금, 나이에 따른 요금을 계산하여 저장한다.")
            void it_returns_fare() {
                MockitoAnnotations.initMocks(this);
                when(subwayPath.distance()).thenReturn(80);
                when(subwayPath.lineExtraFare()).thenReturn(1000);

                SubwayFare subwayFare = new SubwayFare(subwayPath, 30);
                Assertions.assertEquals(subwayFare.getValue(), 3450);
            }
        }

        @Nested
        @DisplayName("지하철 경로와 청소년 승객 나이가 주어지면")
        class Context_teenager_fare {
            @Mock
            private SubwayPath subwayPath;

            @Test
            @DisplayName("이동 거리, 노선 추가 요금, 나이에 따른 요금을 계산하여 저장한다.")
            void it_returns_fare() {
                MockitoAnnotations.initMocks(this);
                when(subwayPath.distance()).thenReturn(80);
                when(subwayPath.lineExtraFare()).thenReturn(1000);

                SubwayFare subwayFare = new SubwayFare(subwayPath, 15);
                Assertions.assertEquals(subwayFare.getValue(), 2480);
            }
        }

        @Nested
        @DisplayName("지하철 경로와 승객의 어린이 승객 나이가 주어지면")
        class Context_children_fare {
            @Mock
            private SubwayPath subwayPath;

            @Test
            @DisplayName("이동 거리, 노선 추가 요금, 나이에 따른 요금을 계산하여 저장한다.")
            void it_returns_fare() {
                MockitoAnnotations.initMocks(this);
                when(subwayPath.distance()).thenReturn(80);
                when(subwayPath.lineExtraFare()).thenReturn(1000);

                SubwayFare subwayFare = new SubwayFare(subwayPath, 6);
                Assertions.assertEquals(subwayFare.getValue(), 1550);
            }
        }
    }

}