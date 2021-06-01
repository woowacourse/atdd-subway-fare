package wooteco.subway.line.dao;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.line.domain.Line;

import javax.print.attribute.standard.MediaSize;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class LineDaoTest {

    private static final Long ID = 2L;
    private static final String NAME = "봉미선";
    private static final String COLOR = "rg-black-100";
    private static final int EXTRA_FARE = 100;
    private static final Line DEFAULT_LINE = new Line(1L, "기본선", "rg-red-100");

    LineDao lineDao;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate);
        lineDao.insert(DEFAULT_LINE);
    }

    @Test
    @DisplayName("Line을 추가한다.")
    void insert() {
        final Line line = lineDao.insert(new Line(NAME, COLOR, EXTRA_FARE));
        assertThat(line.getId()).isEqualTo(ID);
        assertThat(line.getName()).isEqualTo(NAME);
        assertThat(line.getColor()).isEqualTo(COLOR);
        assertThat(line.getExtraFare()).isEqualTo(EXTRA_FARE);
    }

    @Test
    @DisplayName("Id로 Line을 찾아 반환한다.")
    void findById() {
        final Line line = lineDao.findById(1L);
        assertThat(line.getId()).isEqualTo(1L);
        assertThat(line.getName()).isEqualTo("기본선");
        assertThat(line.getColor()).isEqualTo("rg-red-100");
        assertThat(line.getExtraFare()).isEqualTo(0);
    }

    @Test
    @DisplayName("Line을 수정한다.")
    void update() {
        lineDao.update(new Line(1L, NAME, COLOR, EXTRA_FARE));

        final Line line = lineDao.findById(1L);

        assertThat(line.getId()).isEqualTo(1L);
        assertThat(line.getName()).isEqualTo(NAME);
        assertThat(line.getColor()).isEqualTo(COLOR);
        assertThat(line.getExtraFare()).isEqualTo(EXTRA_FARE);
    }

    @Test
    @DisplayName("테이블에 등록된 모든 라인을 가져온다.")
    void findAll() {
        final List<Line> lines = lineDao.findAll();
        assertThat(lines.size()).isEqualTo(1);
        assertThat(lines.get(0).getId()).isEqualTo(1L);
        assertThat(lines.get(0).getName()).isEqualTo(DEFAULT_LINE.getName());
        assertThat(lines.get(0).getColor()).isEqualTo(DEFAULT_LINE.getColor());
    }

    @Test
    @DisplayName("id로 노선을 지운다.")
    void deleteById() {
        lineDao.deleteById(1L);
        assertThat(lineDao.isExistById(1L))
                .isFalse();
    }

    @Test
    @DisplayName("Name으로 존재하는 노선이 있는지 확인하고 Boolean을 확인한다.")
    void isExistByName() {
        assertThat(lineDao.isExistByName(DEFAULT_LINE.getName())).isTrue();
        assertThat(lineDao.isExistByName("없는_이름이지롱")).isFalse();
    }

    @Test
    @DisplayName("Color로 존재하는 노선이 있는지 확인하고 Boolean을 확인한다.")
    void isExistByColor() {
        assertThat(lineDao.isExistByColor(DEFAULT_LINE.getColor())).isTrue();
        assertThat(lineDao.isExistByColor("없는_색이지롱")).isFalse();
    }

    @Test
    @DisplayName("Id로 존재하는 노선이 있는지 확인하고 Boolean을 확인한다.")
    void isExistById() {
        assertThat(lineDao.isExistById(DEFAULT_LINE.getId())).isTrue();
        assertThat(lineDao.isExistById(100L)).isFalse();
    }
}