package wooteco.subway.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ActiveProfiles;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.exception.line.DuplicatedLineNameException;
import wooteco.subway.line.exception.line.NoSuchLineException;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
@Import(LineDao.class)
@ActiveProfiles("test")
public class LineDaoTest {

    private static final String NAME = "name";
    private static final String COLOR = "color";
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    LineDao lineDao;

    @Test
    @DisplayName("정상적인 라인 삽입")
    public void lineInsert() {
        //given
        lineDao.insert(new Line(NAME, COLOR));

        //when & then
        Boolean result = jdbcTemplate.queryForObject("select exists (select * from LINE where name = ?)", Boolean.class, NAME);
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("Id로 라인 조회")
    public void findById() {
        //given
        long id = insert(NAME, COLOR);

        //when
        Line line = lineDao.findById(id);

        //then
        assertThat(line).usingRecursiveComparison()
                .ignoringFields("id", "sections", "extraFare")
                .isEqualTo(new Line(NAME, COLOR));
    }

    @Test
    @DisplayName("수정 확인")
    public void update() {
        //given
        long id = insert(NAME, COLOR);

        //when
        Line newLine = new Line(id, "updatedName", "updatedColor");

        //then
        Line findedLine = lineDao.findById(id);
        assertThat(findedLine).isEqualTo(newLine);
    }

    @Test
    @DisplayName("전체 조회")
    public void findAll() {
        //given
        List<Line> givenLines = Arrays.asList(new Line("a", "a"), new Line("b", "b"), new Line("c", "c"));
        insert("a", "a");
        insert("b", "b");
        insert("c", "c");

        //when
        List<Line> findLines = lineDao.findAll();

        //then
        assertThat(findLines).usingRecursiveComparison()
                .ignoringFields("id", "sections", "extraFare")
                .isEqualTo(givenLines);
    }

    @Test
    @DisplayName("삭제 실패")
    public void failedDeleteById() {
        assertThatThrownBy(() -> lineDao.deleteById(10000L))
                .isInstanceOf(NoSuchLineException.class);
    }

    @Test
    @DisplayName("삭제 성공")
    public void deleteById() {
        //given
        long id = insert(NAME, COLOR);
        Boolean beforeDeletedResult = jdbcTemplate.queryForObject("select exists (select * from LINE where id = ?)", Boolean.class, id);
        assertThat(beforeDeletedResult).isTrue();

        //when
        lineDao.deleteById(id);

        //then
        Boolean afterDeletedResult = jdbcTemplate.queryForObject("select exists (select * from LINE where id = ?)", Boolean.class, id);
        assertThat(afterDeletedResult).isFalse();
    }

    private long insert(String name, String color) {
        String sql = "insert into LINE (name, color) values (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setString(1, name);
            ps.setString(2, color);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}
