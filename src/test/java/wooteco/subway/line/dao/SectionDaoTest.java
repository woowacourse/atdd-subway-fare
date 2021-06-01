package wooteco.subway.line.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class SectionDaoTest {

    private final static Station STATION1 = new Station(1L, "강남역");
    private final static Station STATION2 = new Station(2L, "잠남역");
    private final static Line LINE = new Line(1L, "봉미선", "red");
    private final static Line LINE2 = new Line(2L, "봉미선선", "green");
    private final static Section SECTION = new Section(1L, STATION1, STATION2, 10);
    private final static Section SECTION2 = new Section(2L, STATION1, STATION2, 10);
    private final static Line LINE_WITH_SECTIONS = new Line(1L, "봉미선", "red", new Sections(Arrays.asList(SECTION)));
    SectionDao sectionDao;
    StationDao stationDao;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate);
        stationDao = new StationDao(jdbcTemplate);

        insertStationDummyData();
        insertSectionDummyData();
    }

    private void insertSectionDummyData() {
        sectionDao.insert(LINE, SECTION);
    }

    private void insertStationDummyData() {
        stationDao.insert(STATION1);
        stationDao.insert(STATION2);
    }

    @Test
    @DisplayName("구간을 삽입한다.")
    void insert() {
        final Section insertSection = sectionDao.insert(LINE2, SECTION2);
        assertThat(insertSection.getId()).isEqualTo(2L);
        assertThat(insertSection.getDistance()).isEqualTo(10);
        assertThat(insertSection.getUpStation().getId()).isEqualTo(1L);
        assertThat(insertSection.getDownStation().getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("여러 구간을 삽입한다.")
    void insertSections() {
        assertThatCode(() -> sectionDao.insertSections(LINE_WITH_SECTIONS)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("LineId로 구간이 존재하는 지 확인하고, 있으면 True를 리턴한다.")
    void isExistByLineId() {
        assertThat(sectionDao.isExistByLineId(1L)).isTrue();
    }

    @Test
    @DisplayName("LineId로 구간이 존재하는 지 확인하고, 없으면 False를 리턴한다.")
    void isExistByInvalidLineId() {
        assertThat(sectionDao.isExistByLineId(100L)).isFalse();
    }

    @Test
    @DisplayName("Line에 구간이 포함되어있지 않으면 True를 리턴한다.")
    void isIncludedInLine() {
        assertThat(sectionDao.isIncludedInLine(1L)).isTrue();
    }

    @Test
    @DisplayName("Line에 구간이 포함되어있지 않으면 False를 리턴한다.")
    void isNotIncludedInLine() {
        assertThat(sectionDao.isIncludedInLine(100L)).isFalse();
    }

    @Test
    @DisplayName("역이 포함되어있는 Line의 아이디를 가져온다.")
    void getLineIdIfStationIncluded() {
        final List<Long> lineIds = sectionDao.getLineIdIfStationIncluded(STATION1);
        assertThat(lineIds.size()).isEqualTo(1);
        assertThat(lineIds.get(0)).isEqualTo(1L);
    }
}