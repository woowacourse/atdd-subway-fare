package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.path.infrastructure.SectionEdge;
import wooteco.subway.path.infrastructure.SubwayPath;
import wooteco.subway.station.domain.Station;

public class FareTest {
    private final Station station1 = new Station("강남역");
    private final Station station2 = new Station("잠실역");
    private final Station station3 = new Station("교대역");
    private final List<Station> stations = Arrays.asList(station1, station2, station3);
    private Line line = new Line("2호선", "black");

    @DisplayName("6세 미만의 유아 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {0, 5})
    void babyFare(int age) {
        LoginMember loginMember = new LoginMember(age);
        Fare fare = new Fare(setSubwayPath(5), loginMember.getAge());

        assertThat(fare.getFare()).isEqualTo(0);
    }

    @DisplayName("6 ~ 13세의 어린이 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {6, 12})
    void childrenFare(int age) {
        LoginMember loginMember = new LoginMember(age);
        Fare fare = new Fare(setSubwayPath(5), loginMember.getAge());

        assertThat(fare.getFare()).isEqualTo(450);
    }

    @DisplayName("13 ~ 18의 청소년 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {13, 18})
    void teenagerFare(int age) {
        LoginMember loginMember = new LoginMember(age);
        Fare fare = new Fare(setSubwayPath(5), loginMember.getAge());

        assertThat(fare.getFare()).isEqualTo(720);
    }

    @DisplayName("19세 이상의 일반 요금을 계산한다.")
    @ParameterizedTest
    @ValueSource(ints = {19, 100})
    void adultFare(int age) {
        LoginMember loginMember = new LoginMember(age);
        Fare fare = new Fare(setSubwayPath(5), loginMember.getAge());

        assertThat(fare.getFare()).isEqualTo(1250);
    }

    @DisplayName("기본 운임인 경우")
    @Test
    void defaultDistanceFare() {
        SubwayPath subwayPath = setSubwayPath(5);

        assertThat(new Fare(subwayPath, 20).getFare()).isEqualTo(1250);
        assertThat(subwayPath.getDistance()).isEqualTo(10);
        assertThat(subwayPath.getStations()).containsAll(stations);
    }

    @DisplayName("50km 이하인 경우")
    @Test
    void middleDistanceFare() {
        SubwayPath subwayPath = setSubwayPath(25);

        assertThat(new Fare(subwayPath, 20).getFare()).isEqualTo(2050);
        assertThat(subwayPath.getDistance()).isEqualTo(50);
        assertThat(subwayPath.getStations()).containsAll(stations);
    }

    @DisplayName("50km 초과인 경우")
    @Test
    void extraDistanceFare() {
        SubwayPath subwayPath = setSubwayPath(30);

        assertThat(new Fare(subwayPath, 20).getFare()).isEqualTo(2450);
        assertThat(subwayPath.getDistance()).isEqualTo(60);
        assertThat(subwayPath.getStations()).containsAll(stations);
    }

    @DisplayName("환승이 없는 경우에 요금이 추가되는 경우")
    @Test
    void oneLineExtraFare() {
        Sections sections = new Sections(Arrays.asList(makeSection1(30), makeSection2(30)));
        line = new Line(1L, "testLine1", "black", sections, 900);

        SubwayPath subwayPath = setSubwayPath(30);

        assertThat(new Fare(subwayPath, 20).getFare()).isEqualTo(3350);
    }

    @DisplayName("환승이 있는 경우에 요금이 추가되는 경우")
    @Test
    void twoLineExtraFare() {
        Section section1 = makeSection1(30);
        Section section2 = makeSection2(30);
        Sections sections1 = new Sections(Collections.singletonList(makeSection1(30)));
        Sections sections2 = new Sections(Collections.singletonList(makeSection2(30)));
        line = new Line(1L, "testLine1", "black", sections1, 900);
        Line line1 = new Line(2L, "testLine2", "white", sections2, 1000);
        SectionEdge sectionEdge1 = new SectionEdge(section1, line);
        SectionEdge sectionEdge2 = new SectionEdge(section2, line1);

        SubwayPath subwayPath = new SubwayPath(Arrays.asList(sectionEdge1, sectionEdge2), stations);

        assertThat(new Fare(subwayPath, 20).getFare()).isEqualTo(3450);
        assertThat(subwayPath.getDistance()).isEqualTo(60);
        assertThat(subwayPath.getStations()).containsAll(stations);
    }

    private SubwayPath setSubwayPath(int distance) {
        SectionEdge sectionEdge1 = new SectionEdge(makeSection1(distance), line);
        SectionEdge sectionEdge2 = new SectionEdge(makeSection2(distance), line);

        return new SubwayPath(Arrays.asList(sectionEdge1, sectionEdge2), stations);
    }

    private Section makeSection1(int distance) {
        return new Section(station1, station2, distance);
    }

    private Section makeSection2(int distance) {
        return new Section(station2, station3, distance);
    }
}
