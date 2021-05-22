package wooteco.subway;

import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

public class TestDataLoader {

    Station 강남역;
    Station 판교역;
    Station 정자역;
    Station 역삼역;
    Station 잠실역;
    Section 강남판교;
    Section 판교정자;
    Section 강남역삼;
    Section 역삼잠실;
    Line 신분당선;
    Line 이호선;

    public TestDataLoader() {
        강남역 = new Station(1L, "강남역");
        판교역 = new Station(2L, "판교역");
        정자역 = new Station(3L, "정자역");
        역삼역 = new Station(4L, "역삼역");
        잠실역 = new Station(5L, "잠실역");

        신분당선 = new Line(1L, "신분당선", "red lighten-1");
        강남판교 = new Section(강남역, 판교역, 10);
        판교정자 = new Section(판교역, 정자역, 10);
        신분당선.addSection(강남판교);
        신분당선.addSection(판교정자);

        이호선 = new Line(2L, "2호선", "green lighten-1");
        강남역삼 = new Section(강남역, 역삼역, 10);
        역삼잠실 = new Section(역삼역, 잠실역, 10);
        이호선.addSection(강남역삼);
        이호선.addSection(역삼잠실);
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

    public Line 신분당선() {
        return 신분당선;
    }

    public Line 이호선() {
        return 이호선;
    }
}
