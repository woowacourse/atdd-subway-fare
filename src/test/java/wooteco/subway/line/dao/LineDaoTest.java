package wooteco.subway.line.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class LineDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private LineDao lineDao;
    private StationDao stationDao;
    private SectionDao sectionDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(namedParameterJdbcTemplate, dataSource);
        stationDao = new StationDao(namedParameterJdbcTemplate, dataSource);
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("라인을 생성하면, DB에서 ID를 순서대로 지정하여 저장한다.")
    void insert() {
        final Line line = new Line("노선명", "노선색", 100);
        final Line lineInserted = lineDao.insert(line);
        assertThat(lineInserted.getId()).isEqualTo(1L);
        assertThat(lineInserted.getLineName()).isEqualTo(line.getLineName());
        assertThat(lineInserted.getLineColor()).isEqualTo(line.getLineColor());
        assertThat(lineInserted.getExtraFare()).isEqualTo(line.getExtraFare());
    }

    @Test
    @DisplayName("ID를 통해 라인을 조회할 수 있다.")
    void findById() {
        final Line line = new Line("노선명", "노선색", 100);
        final Line lineInserted = lineDao.insert(line);

        final Optional<Line> lineById = lineDao.findById(lineInserted.getId());
        assertThat(lineById.get().getId()).isEqualTo(lineInserted.getId());
        assertThat(lineById.get().getLineName()).isEqualTo(lineInserted.getLineName());
        assertThat(lineById.get().getLineColor()).isEqualTo(lineInserted.getLineColor());
        assertThat(lineById.get().getExtraFare()).isEqualTo(lineInserted.getExtraFare());
    }

    @Test
    @DisplayName("노선명을 통해 라인을 조회할 수 있다.")
    void findByName() {
        final Line line = new Line("노선명", "노선색", 100);
        final Line lineInserted = lineDao.insert(line);

        final Optional<Line> lineById = lineDao.findByName(lineInserted.getLineName());
        assertThat(lineById.get().getId()).isEqualTo(lineInserted.getId());
        assertThat(lineById.get().getLineName()).isEqualTo(lineInserted.getLineName());
        assertThat(lineById.get().getLineColor()).isEqualTo(lineInserted.getLineColor());
        assertThat(lineById.get().getExtraFare()).isEqualTo(lineInserted.getExtraFare());
    }

    @Test
    @DisplayName("노선 색상을 통해 라인을 조회할 수 있다.")
    void findByColor() {
        final Line line = new Line("노선명", "노선색", 100);
        final Line lineInserted = lineDao.insert(line);

        final Optional<Line> lineById = lineDao.findByColor(lineInserted.getLineColor());
        assertThat(lineById.get().getId()).isEqualTo(lineInserted.getId());
        assertThat(lineById.get().getLineName()).isEqualTo(lineInserted.getLineName());
        assertThat(lineById.get().getLineColor()).isEqualTo(lineInserted.getLineColor());
        assertThat(lineById.get().getExtraFare()).isEqualTo(lineInserted.getExtraFare());
    }

    @Test
    void update() {
        final Line line = new Line("노선명", "노선색", 100);
        final Line lineInserted = lineDao.insert(line);

        Optional<Line> lineById = lineDao.findById(lineInserted.getId());
        assertThat(lineById.get().getId()).isEqualTo(lineInserted.getId());
        assertThat(lineById.get().getLineName()).isEqualTo(lineInserted.getLineName());
        assertThat(lineById.get().getLineColor()).isEqualTo(lineInserted.getLineColor());
        assertThat(lineById.get().getExtraFare()).isEqualTo(lineInserted.getExtraFare());

        final Line newLine = new Line(lineInserted.getId(), "바뀐명", "바뀐색", 900);
        lineDao.update(newLine);

        lineById = lineDao.findById(lineInserted.getId());
        assertThat(lineById.get().getId()).isEqualTo(lineInserted.getId());
        assertThat(lineById.get().getLineName()).isEqualTo(newLine.getLineName());
        assertThat(lineById.get().getLineColor()).isEqualTo(newLine.getLineColor());
        assertThat(lineById.get().getExtraFare()).isEqualTo(newLine.getExtraFare());
    }

    @Test
    @DisplayName("모든 노선을 조회할 수 있다.")
    void findAll() {
        final Line lineInserted1 = lineDao.insert(new Line("노선명1", "노선색1", 100));
        final Line lineInserted2 = lineDao.insert(new Line("노선명2", "노선색2", 100));
        final Line lineInserted3 = lineDao.insert(new Line("노선명3", "노선색3", 100));
        final Line lineInserted4 = lineDao.insert(new Line("노선명4", "노선색4", 100));

        final List<Line> allLines = lineDao.findAll();
        final List<String> lineNames = allLines.stream()
                .map(Line::getLineName)
                .collect(Collectors.toList());

        assertThat(lineNames).containsExactly(lineInserted1.getLineName(), lineInserted2.getLineName(),
                lineInserted3.getLineName(), lineInserted4.getLineName());
    }

    @Test
    @DisplayName("해당 역이 존재하는 노선을 조회해올 수 있다.")
    void findIncludingStation() {
        final Station 테스트역1 = stationDao.insert(new Station("테스트역1"));
        final Station 테스트역2 = stationDao.insert(new Station("테스트역2"));
        final Section section = new Section(1L, 테스트역1, 테스트역2, 10);

        final Line line = new Line(1L, "노선명1", "노선색1", 100, new Sections(Arrays.asList(section)));
        lineDao.insert(line);
        sectionDao.insert(line, section);

        final List<Line> lineIncludingStation = lineDao.findIncludingStation(테스트역1.getId());

        assertThat(lineIncludingStation).hasSize(1);
        assertThat(lineIncludingStation.get(0).getLineName()).isEqualTo(line.getLineName());
        assertThat(lineIncludingStation.get(0).getLineColor()).isEqualTo(line.getLineColor());
        assertThat(lineIncludingStation.get(0).getExtraFare()).isEqualTo(line.getExtraFare());
    }

    @Test
    @DisplayName("Id를 통해 노선을 삭제할 수 있다")
    void deleteById() {
        final Line line = new Line("노선명", "노선색", 100);
        final Line lineInserted = lineDao.insert(line);

        lineDao.deleteById(lineInserted.getId());

        final Optional<Line> lineById = lineDao.findById(lineInserted.getId());
        assertThat(lineById.isPresent()).isFalse();
    }
}