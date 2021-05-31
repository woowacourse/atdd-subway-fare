package wooteco.subway.line.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import wooteco.subway.exception.notfound.LineNotFoundException;
import wooteco.subway.exception.notfound.MemberNotFoundException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.Fare;
import wooteco.subway.path.domain.Money;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJdbcTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class LineDaoTest {
    private StationDao stationDao;
    private LineDao lineDao;
    private Line line1;
    private Line line2;

    public LineDaoTest(DataSource dataSource) {
        stationDao = new StationDao(new JdbcTemplate(dataSource), dataSource);
        lineDao = new LineDao(new JdbcTemplate(dataSource), dataSource);
    }

    @BeforeEach
    void setUp() {
        stationDao.insert(new Station("강남역"));
        stationDao.insert(new Station("역삼역"));

        line1 = new Line("신분당선", "bg-red-600", Fare.of(new Money(100)));
        line2 = new Line("2호선", "bg-red-400", Fare.of(new Money(300)));
    }

    @DisplayName("지하철 노선정보 저장한다.")
    @Test
    void insert() {
        //when
        Line insertLine = lineDao.insert(line1);

        //then
        assertThat(insertLine.getName()).isEqualTo(line1.getName());
        assertThat(insertLine.getColor()).isEqualTo(line1.getColor());
        assertThat(insertLine.getExtraFare()).isEqualTo(line1.getExtraFare());
    }

    @DisplayName("이미 존재하는 이름의 지하철 노선정보 저장한다.")
    @Test
    void insertByDuplicateName() {
        // given
        lineDao.insert(line1);

        //when then
        assertThatThrownBy(() -> lineDao.insert(new Line(line1.getName(), line2.getColor(), line2.getExtraFare())))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("이미 존재하는 색깔의 지하철 노선정보 저장한다.")
    @Test
    void insertByDuplicateColor() {
        // given
        lineDao.insert(line1);

        //when then
        assertThatThrownBy(() -> lineDao.insert(new Line(line2.getName(), line1.getColor(), line2.getExtraFare())))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("지하철 노선 ID로 노선 정보 가져온다.")
    @Test
    void findById() {
        //given
        Line insertLine = lineDao.insert(line1);

        //when
        Line findLine = lineDao.findById(insertLine.getId()).orElseThrow(MemberNotFoundException::new);

        //then
        assertThat(findLine.getId()).isEqualTo(insertLine.getId());
        assertThat(findLine.getName()).isEqualTo(insertLine.getName());
        assertThat(findLine.getColor()).isEqualTo(insertLine.getColor());
        assertThat(findLine.getExtraFare()).isEqualTo(insertLine.getExtraFare());
    }

    @DisplayName("존재하지 않는 지하철 노선 ID로 노선 정보 조회한다.")
    @Test
    void findByNotExistId() {
        //when
        Optional<Line> findLine = lineDao.findById(line1.getId());

        //then
        assertThat(findLine).isEqualTo(Optional.empty());
    }

    @DisplayName("지하철 노선 정보 수정한다.")
    @Test
    void update() {
        //given
        Line insertLine = lineDao.insert(line1);
        Long savedLineId = insertLine.getId();

        //when
        lineDao.update(new Line(insertLine.getId(), line2.getName(), line2.getColor(), line2.getExtraFare()));
        Line updatedLine = lineDao.findById(savedLineId).orElseThrow(LineNotFoundException::new);

        //then
        assertThat(updatedLine.getId()).isEqualTo(savedLineId);
        assertThat(updatedLine.getName()).isEqualTo(line2.getName());
        assertThat(updatedLine.getColor()).isEqualTo(line2.getColor());
        assertThat(updatedLine.getExtraFare()).isEqualTo(line2.getExtraFare());
    }

    @DisplayName("지하철 노선 정보를 중복되는 이름으로 수정한다.")
    @Test
    void updateByDuplicateName() {
        //given
        lineDao.insert(line1);
        Line insertLine = lineDao.insert(line2);

        //when then
        assertThatThrownBy(() ->
                lineDao.update(new Line(
                        insertLine.getId(),
                        line1.getName(),
                        insertLine.getColor(),
                        insertLine.getExtraFare())))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("지하철 노선 정보를 중복되는 색깔로 수정한다.")
    @Test
    void updateByDuplicateColor() {
        //given
        lineDao.insert(line1);
        Line insertLine = lineDao.insert(line2);

        //when then
        assertThatThrownBy(() ->
                lineDao.update(new Line(
                        insertLine.getId(),
                        insertLine.getName(),
                        line1.getColor(),
                        insertLine.getExtraFare())))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("전체 노선 정보를 가져온다.")
    @Test
    void findAll() {
        //given
        List<Line> beforeSaveLines = lineDao.findAll();
        lineDao.insert(line1);
        lineDao.insert(line2);

        //when
        List<Line> lines = lineDao.findAll();

        //then
        assertThat(beforeSaveLines).hasSize(0);
        assertThat(lines).hasSize(2);
    }

    @DisplayName("노선 ID로 노선 데이터 삭제한다.")
    @Test
    void deleteById() {
        //given
        Line insertLine = lineDao.insert(line1);
        Long savedLineId = insertLine.getId();

        //when
        lineDao.deleteById(savedLineId);
        Optional<Line> findLine = lineDao.findById(savedLineId);

        //then
        assertThat(findLine).isEqualTo(Optional.empty());
    }

    @DisplayName("존재하지 않는 노선 ID로 노선 데이터 삭제한다.")
    @Test
    void deleteByNotExistId() {
        //given
        Line insertLine = lineDao.insert(line1);
        Long savedLineId = insertLine.getId();

        //when
        lineDao.deleteById(savedLineId);
        Optional<Line> findLine = lineDao.findById(savedLineId);

        //then
        assertThat(findLine).isEqualTo(Optional.empty());
    }

    @DisplayName("존재하는 노선 이름으로 노선 데이터가 존재하는지 검사한다.")
    @Test
    void existLineName() {
        //given
        lineDao.insert(line1);

        //when
        boolean existLineName = lineDao.existLineName(line1.getName());

        //then
        assertThat(existLineName).isTrue();
    }

    @DisplayName("존재하지 않는 노선 이름으로 노선 데이터가 존재하는지 검사한다.")
    @Test
    void existLineNameByNotExistName() {
        //when
        boolean existLineName = lineDao.existLineName(line1.getName());

        //then
        assertThat(existLineName).isFalse();
    }

    @DisplayName("존재하는 노선 색깔로 노선 데이터가 존재하는지 검사한다.")
    @Test
    void existLineColor() {
        //given
        lineDao.insert(line1);

        //when
        boolean existLineColor = lineDao.existLineColor(line1.getColor());

        //then
        assertThat(existLineColor).isTrue();
    }

    @DisplayName("존재하지 않는 노선 색깔로 노선 데이터가 존재하는지 검사한다.")
    @Test
    void existLineColorByNotExist() {
        //when
        boolean existLineColor = lineDao.existLineColor(line1.getColor());

        //then
        assertThat(existLineColor).isFalse();
    }
}