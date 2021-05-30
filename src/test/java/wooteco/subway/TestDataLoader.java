package wooteco.subway;

import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.member.domain.LoginMember;
import wooteco.subway.station.domain.Station;

public class TestDataLoader {

    private int zeroExtraFare = 0;

    public Line 신분당선() {
        Line 신분당선 = new Line(1L, "신분당선", "red lighten-1", zeroExtraFare);
        신분당선.addSection(new Section(강남역(), 판교역(), 5));
        신분당선.addSection(new Section(판교역(), 정자역(), 5));
        return 신분당선;
    }

    public Line 이호선() {
        Line 이호선 = new Line(2L, "2호선", "green lighten-1", zeroExtraFare);
        이호선.addSection(new Section(강남역(), 역삼역(), 25));
        이호선.addSection(new Section(역삼역(), 잠실역(), 25));
        return 이호선;
    }

    public Line 사호선() {
        Line 사호선 = new Line(3L, "4호선", "blue lighten-1", zeroExtraFare);
        사호선.addSection(new Section(산본역(), 금정역(), 50));
        사호선.addSection(new Section(금정역(), 범계역(), 50));
        return 사호선;
    }

    public Station 강남역() {
        return new Station(1L, "강남역");
    }

    public Station 판교역() {
        return new Station(2L, "판교역");
    }

    public Station 정자역() {
        return new Station(3L, "정자역");
    }

    public Station 역삼역() {
        return new Station(4L, "역삼역");
    }

    public Station 잠실역() {
        return new Station(5L, "잠실역");
    }

    public Station 산본역() {
        return new Station(6L, "산본역");
    }

    public Station 금정역() {
        return new Station(7L, "금정역");
    }

    public Station 범계역() {
        return new Station(8L, "범계역");
    }
}
