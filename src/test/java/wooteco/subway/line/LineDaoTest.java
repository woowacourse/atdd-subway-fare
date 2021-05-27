package wooteco.subway.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
public class LineDaoTest {
    private LineDao lineDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        this.lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @DisplayName("중복되는 이름을 가지는 노선을 추가할 시 DuplicateKeyException이 발생한다.")
    @Test
    void throw_DuplicateKeyException_When_Insert_DuplicateName() {
        lineDao.insert(new Line("신분당선", "bg-red-600", 1200));
        assertThatThrownBy(() -> lineDao.insert(new Line("신분당선", "black", 1200)))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("중복되는 색깔을 가지는 노선을 추가할 시 DuplicateKeyException이 발생한다.")
    @Test
    void throw_DuplicateKeyException_When_Insert_DuplicateColor() {
        lineDao.insert(new Line("신분당선", "bg-red-600", 1200));
        assertThatThrownBy(() -> lineDao.insert(new Line("2호선", "bg-red-600", 1200)))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("존재하지 않는 지하철 노선을 검색할 시 RuntimeException이 발생한다.")
    @Test
    void throw_NoSuchElementException_When_Find_NonExists() {
        assertThatThrownBy(() -> lineDao.findById(1L))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("존재하지 않는 지하철 노선을 삭제할 시 affectedRow가 0이다.")
    @Test
    void throw_NoSuchElementException_When_Delete_NonExists() {
        assertThat(lineDao.deleteById(1L)).isEqualTo(0);
    }
}
