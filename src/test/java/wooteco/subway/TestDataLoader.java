package wooteco.subway;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

public class TestDataLoader {

    Station 강남역;
    Station 판교역;
    Station 정자역;
    Station 역삼역;
    Station 잠실역;
    Line 신분당선;
    Line 이호선;


    public TestDataLoader() {
        강남역 = new Station(1L, "강남역");
        판교역 = new Station(2L, "판교역");
        정자역 = new Station(3L, "정자역");
        역삼역 = new Station(4L, "역삼역");
        잠실역 = new Station(5L, "잠실역");

        신분당선 = new Line(1L, "신분당선", "red lighten-1");
        신분당선.addSection(new Section(1L, 강남역, 판교역, 10));
        신분당선.addSection(new Section(2L, 판교역, 정자역, 10));

        이호선 = new Line(2L, "2호선", "green lighten-1");
        이호선.addSection(new Section(3L, 강남역, 역삼역, 10));
        이호선.addSection(new Section(4L, 역삼역, 잠실역, 10));
    }

    public Line 신분당선() {
        return 신분당선;
    }

    public Line 이호선() {
        return 이호선;
    }

    public Station 강남역() {
        return 강남역;
    }

    public Station 판교역() {
        return 판교역;
    }

    public Station 정자역() {
        return 정자역;
    }

    public Station 역삼역() {
        return 역삼역;
    }

    public Station 잠실역() {
        return 잠실역;
    }
}
