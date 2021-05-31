package wooteco.subway.path.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.fare.domain.AgeDiscountStrategy;
import wooteco.subway.fare.domain.DistanceFareStrategy;
import wooteco.subway.fare.domain.Fare;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.application.PathFinder;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class SubwayPathTest {

    private final PathFinder pathFinder = new PathFinder();
    private Station 강남역 = new Station(1L, "강남역");
    private Station 잠실역 = new Station(2L, "잠실역");
    private Station 송파역 = new Station(3L, "송파역");

    @DisplayName("현재 경로에 포함된 노선들 중 가장 높은 노선 추가 요금을 확인한다.")
    @Test
    void calculateLineFare() {
        Section section1 = new Section(1L, 강남역, 잠실역, 5);
        Section section2 = new Section(2L, 잠실역, 송파역, 5);
        Sections sections1 = new Sections(Arrays.asList(section1));
        Sections sections2 = new Sections(Arrays.asList(section2));
        Line 이호선 = new Line(1L, "2호선", "black", 900, sections1);
        Line 삼호선 = new Line(2L, "3호선", "white", 300, sections2);

        SubwayPath subwayPath = pathFinder.findPath(Arrays.asList(이호선, 삼호선), 강남역, 송파역);
        int lineExtraFare = subwayPath.calculateLineFare();

        assertThat(lineExtraFare).isEqualTo(900);
    }

    @Nested
    @DisplayName("calculateFare 메서드는")
    class Describe_calculateFare {

        @Nested
        @DisplayName("10km 이하일 때는")
        class Context_under10km {

            private SubwayPath subwayPath;

            @BeforeEach
            void setUp() {
                Section section1 = new Section(1L, 강남역, 잠실역, 5);
                Section section2 = new Section(2L, 잠실역, 송파역, 5);
                Sections sections1 = new Sections(Arrays.asList(section1));
                Sections sections2 = new Sections(Arrays.asList(section2));
                Line 이호선 = new Line(1L, "2호선", "black", 900, sections1);
                Line 삼호선 = new Line(2L, "3호선", "white", 300, sections2);
                subwayPath = pathFinder.findPath(Arrays.asList(이호선, 삼호선), 강남역, 송파역);
            }

            @DisplayName("기본 운임료 1250원만 나온다.")
            @Test
            void calculateBasicFare() {
                int distance = subwayPath.calculateDistance();

                Fare fare = new Fare(distance, 0, DistanceFareStrategy.find(distance));
                int fareValue = fare.calculateFare();

                assertThat(fareValue).isEqualTo(1250);
            }

            @DisplayName("노선 추가 요금이 있을때, 기본 운임료 1250 + 노선추가요금이 발생한다.")
            @Test
            void calculateBasicFareWithLineExtraFare() {
                int distance = subwayPath.calculateDistance();

                Fare fare = new Fare(distance, subwayPath.calculateLineFare(), DistanceFareStrategy.find(distance));
                int fareValue = fare.calculateFare();

                assertThat(fareValue).isEqualTo(2150);
            }
        }

        @Nested
        @DisplayName("10km 초과 50km 이하까지는")
        class Context_over10kmAndUnder50km {

            @DisplayName("기본 운임료 + 5km마다 100원씩 붙는다.")
            @Test
            void calculateWeightedFare_15KM() {
                Section section1 = new Section(1L, 강남역, 잠실역, 10);
                Section section2 = new Section(2L, 잠실역, 송파역, 5);
                Sections sections = new Sections(Arrays.asList(section1, section2));
                Line line = new Line(1L, "2호선", "black", 0, sections);

                SubwayPath path = pathFinder.findPath(Arrays.asList(line), 강남역, 송파역);
                int distance = path.calculateDistance();
                Fare fare = new Fare(distance, 0, DistanceFareStrategy.find(distance));
                int fareValue = fare.calculateFare();

                assertThat(fareValue).isEqualTo(1350);
            }

            @DisplayName("기본 운임료 + 5km마다 100원을 붙을 때, 단 1km만 초과해도 추가 요금이 붙는다.")
            @Test
            void calculateWeightedFare() {
                Section section1 = new Section(1L, 강남역, 잠실역, 10);
                Section section2 = new Section(2L, 잠실역, 송파역, 7);
                Sections sections = new Sections(Arrays.asList(section1, section2));
                Line line = new Line(1L, "2호선", "black", 0, sections);

                SubwayPath path = pathFinder.findPath(Arrays.asList(line), 강남역, 송파역);
                int distance = path.calculateDistance();
                Fare fare = new Fare(distance, 0, DistanceFareStrategy.find(distance));
                int fareValue = fare.calculateFare();

                assertThat(fareValue).isEqualTo(1450);
            }
        }

        @Nested
        @DisplayName("50km 초과일 때는")
        class Context_over50km {

            @DisplayName("50km 초과시 8km 마다 100원 추가된다.")
            @Test
            void calculateOver50kmFare() {
                Section section1 = new Section(1L, 강남역, 잠실역, 10);
                Section section2 = new Section(2L, 잠실역, 송파역, 41);
                Sections sections = new Sections(Arrays.asList(section1, section2));
                Line 이호선 = new Line(1L, "2호선", "black", 0, sections);

                SubwayPath subwayPath = pathFinder.findPath(Arrays.asList(이호선), 강남역, 송파역);
                int distance = subwayPath.calculateDistance();
                Fare fare = new Fare(distance, 0, DistanceFareStrategy.find(distance));
                int fareValue = fare.calculateFare();

                assertThat(fareValue).isEqualTo(2150);
            }
        }

        @DisplayName("나이 할인이 적용될 때")
        @Nested
        class Context_AgeDiscount {

            private SubwayPath subwayPath;

            @BeforeEach
            void setUp() {
                Section section1 = new Section(1L, 강남역, 잠실역, 5);
                Section section2 = new Section(2L, 잠실역, 송파역, 5);
                Sections sections1 = new Sections(Arrays.asList(section1));
                Sections sections2 = new Sections(Arrays.asList(section2));
                Line 이호선 = new Line(1L, "2호선", "black", 900, sections1);
                Line 삼호선 = new Line(2L, "3호선", "white", 300, sections2);
                subwayPath = pathFinder.findPath(Arrays.asList(이호선, 삼호선), 강남역, 송파역);
            }

            @DisplayName("6세 미만인 경우 무료다.")
            @Test
            void isFree() {
                int distance = subwayPath.calculateDistance();
                DistanceFareStrategy strategy = DistanceFareStrategy.find(distance);

                Fare fare = new Fare(distance, subwayPath.calculateLineFare(), strategy, AgeDiscountStrategy.find(5));
                int fareValue = fare.calculateFare();

                assertThat(fareValue).isZero();
            }

            @DisplayName("6세 이상 13세 미만인 경우 350원 차감한 금액의 50%를 할인한다.")
            @ParameterizedTest
            @ValueSource(ints = {6, 12})
            void isChildren(int age) {
                int distance = subwayPath.calculateDistance();
                DistanceFareStrategy strategy = DistanceFareStrategy.find(distance);

                Fare fare = new Fare(distance, subwayPath.calculateLineFare(), strategy, AgeDiscountStrategy.find(age));
                int fareValue = fare.calculateFare();

                assertThat(fareValue).isEqualTo(900);
            }

            @DisplayName("13세 이상 19세 미만은 350원 차감한 금액의 20%를 할인한다.")
            @ParameterizedTest
            @ValueSource(ints = {13, 18})
            void isTeenager(int age) {
                int distance = subwayPath.calculateDistance();
                DistanceFareStrategy strategy = DistanceFareStrategy.find(distance);

                Fare fare = new Fare(distance, subwayPath.calculateLineFare(), strategy, AgeDiscountStrategy.find(age));
                int fareValue = fare.calculateFare();

                assertThat(fareValue).isEqualTo(1440);
            }

            @DisplayName("19세 이상은 기본 요금 정책이다.")
            @ParameterizedTest
            @ValueSource(ints = {19, 23})
            void isAdult(int age) {
                int distance = subwayPath.calculateDistance();
                DistanceFareStrategy strategy = DistanceFareStrategy.find(distance);

                Fare fare = new Fare(distance, subwayPath.calculateLineFare(), strategy, AgeDiscountStrategy.find(age));
                int fareValue = fare.calculateFare();

                assertThat(fareValue).isEqualTo(2150);
            }
        }
    }
}
