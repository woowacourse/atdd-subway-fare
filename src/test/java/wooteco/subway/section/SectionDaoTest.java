package wooteco.subway.section;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import wooteco.subway.exception.NoSuchSectionException;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.section.dao.SectionDao;
import wooteco.subway.section.domain.Distance;
import wooteco.subway.section.domain.Id;
import wooteco.subway.section.domain.Section;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SectionDaoTest {

    private final Line 일호선 = new Line(1L, "1호선", "bg-blue-100", 0);
    private final Station 신설역 = new Station(1L, "신설역");
    private final Station 동묘역 = new Station(2L, "동묘역");
    private final Station 동대문역 = new Station(3L, "동대문역");
    private final Distance 거리 = new Distance(10);

    @Autowired
    private SectionDao sectionDao;
    @Autowired
    private LineDao lineDao;
    @Autowired
    private StationDao stationDao;

    @BeforeEach
    void setUp() {
        lineDao.insert(일호선);
        stationDao.insert(신설역);
        stationDao.insert(동묘역);
        stationDao.insert(동대문역);

        Section 신설역_동묘역 = new Section(일호선, 신설역, 동묘역, 거리);
        Section 동묘역_동대문역 = new Section(일호선, 동묘역, 동대문역, 거리);

        sectionDao.insert(신설역_동묘역);
        sectionDao.insert(동묘역_동대문역);
    }

    @Test
    @DisplayName("Section 저장 테스트")
    public void save() {
        // given
        Section targetSection = new Section(일호선, 신설역, 동대문역, 거리);

        // when
        Section savedSection = sectionDao.insert(targetSection);

        // then
        assertThat(savedSection.getLine()).isEqualTo(일호선);
        assertThat(savedSection.getUpStation()).isEqualTo(신설역);
        assertThat(savedSection.getDownStation()).isEqualTo(동대문역);
        assertThat(savedSection.getDistance()).isEqualTo(거리);
    }

    @Test
    @DisplayName("노선에 있는 Section 들 반환 테스트")
    public void findAllByLine() {
        // given

        // when
        List<Section> sections = sectionDao.findAllByLineId(일호선.getId());
        Section 저장되어있는_신설_동묘_구간 = sections.get(0);
        Section 저장되어있는_동묘_동대문_구간 = sections.get(1);

        // then
        assertThat(저장되어있는_신설_동묘_구간.getLine()).isEqualTo(일호선);
        assertThat(저장되어있는_신설_동묘_구간.getUpStation()).isEqualTo(신설역);
        assertThat(저장되어있는_신설_동묘_구간.getDownStation()).isEqualTo(동묘역);
        assertThat(저장되어있는_신설_동묘_구간.getDistance()).isEqualTo(거리);
        assertThat(저장되어있는_동묘_동대문_구간.getLine()).isEqualTo(일호선);
        assertThat(저장되어있는_동묘_동대문_구간.getUpStation()).isEqualTo(동묘역);
        assertThat(저장되어있는_동묘_동대문_구간.getDownStation()).isEqualTo(동대문역);
        assertThat(저장되어있는_동묘_동대문_구간.getDistance()).isEqualTo(거리);
    }

    @Test
    @DisplayName("노선에 있는 상행역 조회 테스트")
    public void findByLineIdAndUpStation() {
        // given

        // when
        Section section = sectionDao.findByLineIdAndUpStationId(일호선.getId(), 신설역.getId())
            .orElseThrow(NoSuchSectionException::new);

        // then
        assertThat(section.getLine()).isEqualTo(일호선);
        assertThat(section.getUpStation()).isEqualTo(신설역);
        assertThat(section.getDownStation()).isEqualTo(동묘역);
        assertThat(section.getDistance()).isEqualTo(거리);
    }

    @Test
    @DisplayName("노선에 있는 하행역 조회 테스트")
    public void findByLineIdAndDownStation() {
        // given

        // when
        Section section = sectionDao.findByLineIdAndDownStationId(일호선.getId(), 동묘역.getId())
            .orElseThrow(NoSuchSectionException::new);

        // then
        assertThat(section.getLine()).isEqualTo(일호선);
        assertThat(section.getUpStation()).isEqualTo(신설역);
        assertThat(section.getDownStation()).isEqualTo(동묘역);
        assertThat(section.getDistance()).isEqualTo(거리);
    }

    @Test
    @DisplayName("노선에 있는 상/하행역 조회 예외처리")
    public void findByLineIdAndStation() {
        // given
        Station 없는역 = new Station(9L, "없는역");
        Line 없는노선 = new Line(2L, "없는노선", "bg-white-999", 0);

        // when

        // then
        assertThat(sectionDao.findByLineIdAndUpStationId(일호선.getId(), 없는역.getId())).isEmpty();
        assertThat(sectionDao.findByLineIdAndUpStationId(없는노선.getId(), 동묘역.getId())).isEmpty();
        assertThat(sectionDao.findByLineIdAndDownStationId(없는노선.getId(), 동묘역.getId())).isEmpty();
        assertThat(sectionDao.findByLineIdAndUpStationId(일호선.getId(), 동대문역.getId())).isEmpty();
    }

    @Test
    @DisplayName("구간 중 상행역 제거")
    public void deleteByLineIdAndUpStation() {
        // given

        // when
        List<Section> 제거전_전체_역 = sectionDao.findAllByLineId(일호선.getId());
        int 제거된_역_개수 = sectionDao.deleteByLineIdAndUpStationId(일호선.getId(), 신설역.getId());
        List<Section> 제거후_전체_역 = sectionDao.findAllByLineId(일호선.getId());

        //then
        assertThat(제거된_역_개수).isEqualTo(1);
        assertThat(제거전_전체_역).hasSize(2);
        assertThat(제거후_전체_역).hasSize(1);
    }

    @Test
    @DisplayName("구간 중 하행역 제거")
    public void deleteByLineIdAndDownStation() {
        // given

        // when
        List<Section> 제거전_전체_역 = sectionDao.findAllByLineId(일호선.getId());
        int 제거된_역_개수 = sectionDao.deleteByLineIdAndDownStationId(일호선.getId(), 동대문역.getId());
        List<Section> 제거후_전체_역 = sectionDao.findAllByLineId(일호선.getId());

        //then
        assertThat(제거된_역_개수).isEqualTo(1);
        assertThat(제거전_전체_역).hasSize(2);
        assertThat(제거후_전체_역).hasSize(1);
    }

    @Test
    @DisplayName("구간 제거")
    public void delete() {
        // given
        Section section = new Section(new Id(2L), 일호선, 동묘역, 동대문역, 거리);

        // when
        List<Section> 제거전_전체_역 = sectionDao.findAllByLineId(일호선.getId());
        int 제거된_역_개수 = sectionDao.delete(section);
        List<Section> 제거후_전체_역 = sectionDao.findAllByLineId(일호선.getId());

        // then
        assertThat(제거된_역_개수).isEqualTo(1);
        assertThat(제거전_전체_역).hasSize(2);
        assertThat(제거후_전체_역).hasSize(1);
    }

    @Test
    @DisplayName("지하철 역을 통해 환승 가능한 노선 찾기")
    void findIncludeStationLine() {
        // given
        Line 이호선 = new Line(2L, "2호선", "bg-green-200", 0);
        Line 삼호선 = new Line(3L, "3호선", "bg-green-200", 0);
        lineDao.insert(이호선);
        lineDao.insert(삼호선);

        Station 잠실역 = new Station(4L, "잠실역");
        Station 건대입구역 = new Station(5L, "건대입구역");
        Station 면목역 = new Station(6L, "면목역");
        stationDao.insert(잠실역);
        stationDao.insert(건대입구역);
        stationDao.insert(면목역);

        Section 동묘역_잠실역 = new Section(이호선, 동묘역, 잠실역, 거리);
        Section 면목역_잠실역 = new Section(삼호선, 면목역, 잠실역, 거리);
        Section 잠실역_건대입구역 = new Section(삼호선, 잠실역, 건대입구역, 거리);
        sectionDao.insert(동묘역_잠실역);
        sectionDao.insert(면목역_잠실역);
        sectionDao.insert(잠실역_건대입구역);

        // when
        List<Line> lines = sectionDao.findIncludeStationLine(4L); // 잠실역

        // then
        assertThat(lines).containsExactly(이호선, 삼호선);
    }
}
