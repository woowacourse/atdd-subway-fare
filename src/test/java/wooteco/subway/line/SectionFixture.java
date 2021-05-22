package wooteco.subway.line;

import wooteco.subway.line.domain.Section;

import static wooteco.subway.station.StationFixture.*;

public class SectionFixture {
    public static Section 왕십리_잠실_거리22 = new Section(왕십리역, 잠실역, 22);
    public static Section 잠실_강남_거리25 = new Section( 잠실역, 강남역, 25);
    public static Section 강남_구의_거리10 = new Section(강남역, 구의역, 10);
}
