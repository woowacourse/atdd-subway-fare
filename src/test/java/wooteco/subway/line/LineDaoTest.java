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
    void throw_DuplicateKeyException_When_Insert_NonExists() {
        lineDao.insert(new Line("신분당선", "bg-red-600", 1200));
        assertThatThrownBy(() -> lineDao.insert(new Line("신분당선", "black", 1200)))
                .isInstanceOf(DuplicateKeyException.class);
    }
}
