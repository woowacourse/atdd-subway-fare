package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

    @Nested
    @DisplayName("calculateFare 메서드는")
    class Describe_calculateFare {

        @Nested
        @DisplayName("10km 이하일 때는")
        class Context_uner10km {

            @DisplayName("기본 운임료 1250원만 나온다.")
            @Test
            void calculateBasicFare() {
                // section
                Section section = new Section(1L, 강남역, 잠실역, 10);
                Sections sections = new Sections(Arrays.asList(section));
                // line
                Line 이호선 = new Line(1L, "2호선", "black", sections);
                // PathFinder
                SubwayPath subwayPath = pathFinder.findPath(Arrays.asList(이호선), 강남역, 잠실역);
                Fare fare = new Fare(subwayPath.calculateDistance());
                int fareValue = fare.calculateFare();

                assertThat(fareValue).isEqualTo(1250);
            }
        }

        @Nested
        @DisplayName("10km 초과 50km 이하까지는")
        class Context_over10KmandUnder50km {

            @DisplayName("기본 운임료 + 5km마다 100원씩 붙는다.")
            @Test
            void calculateWeightedFare() {
                Section section1 = new Section(1L, 강남역, 잠실역, 10);
                Section section2 = new Section(2L, 잠실역, 송파역, 7);
                Sections sections = new Sections(Arrays.asList(section1, section2));

                Line line = new Line(1L, "2호선", "black", sections);
                SubwayPath path = pathFinder.findPath(Arrays.asList(line), 강남역, 송파역);
                Fare fare = new Fare(path.calculateDistance());
                int fareValue = fare.calculateFare();

                assertThat(fareValue).isEqualTo(1450);
            }

            @DisplayName("기본 운임료 + 5km마다 100원씩 붙는다.")
            @Test
            void calculateWeightedFare_15KM() {
                Section section1 = new Section(1L, 강남역, 잠실역, 10);
                Section section2 = new Section(2L, 잠실역, 송파역, 5);
                Sections sections = new Sections(Arrays.asList(section1, section2));

                Line line = new Line(1L, "2호선", "black", sections);
                SubwayPath path = pathFinder.findPath(Arrays.asList(line), 강남역, 송파역);
                Fare fare = new Fare(path.calculateDistance());
                int fareValue = fare.calculateFare();

                assertThat(fareValue).isEqualTo(1350);
            }

        }

        @Nested
        @DisplayName("50km 초과일 때는")
        class Context_over50km {

            @DisplayName("50km 초과시 8km 마다 100원 추가")
            @Test
            void calculateOver50kmFare() {
                // section
                Section section1 = new Section(1L, 강남역, 잠실역, 10);
                Section section2 = new Section(2L, 잠실역, 송파역, 48);
                Sections sections = new Sections(Arrays.asList(section1, section2));
                // line
                Line 이호선 = new Line(1L, "2호선", "black", sections);
                // PathFinder
                SubwayPath subwayPath = pathFinder.findPath(Arrays.asList(이호선), 강남역, 송파역);
                Fare fare = new Fare(subwayPath.calculateDistance());
                int fareValue = fare.calculateFare();

                assertThat(fareValue).isEqualTo(2150);
            }
        }
    }
}