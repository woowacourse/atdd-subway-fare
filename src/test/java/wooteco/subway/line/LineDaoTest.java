package wooteco.subway.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class LineDaoTest {

    @Autowired
    private LineDao lineDao;

    @DisplayName("노선 저장")
    @Test
    public void save() {
        // given
        Line line = new Line("10호선", "붉은색", 2_000);

        // when
        Line requestedLine = lineDao.insert(line);

        // then
        assertThat(requestedLine.getName()).isEqualTo(line.getName());
        assertThat(requestedLine.getColor()).isEqualTo(line.getColor());
        assertThat(requestedLine.getExtraFare()).isEqualTo(line.getExtraFare());
    }

    @DisplayName("노선 중복 저장 시도")
    @Test
    public void duplicatedSave() {
        // given
        Line line1 = new Line("1호선", "초록색", 0);
        Line line2 = new Line("1호선", "파란색", 0);

        // when
        lineDao.insert(line1);

        // then
        assertThatThrownBy(() -> lineDao.insert(line2)).isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("id값에 맞는 노선 반환")
    @Test
    public void findLine() {
        // given
        Line line1 = new Line("12호선", "분홍색", 3_000);
        Line saveLine = lineDao.insert(line1);
        Long id = saveLine.getId();

        // when
        Line requestedLine = lineDao.find(id);

        // then
        assertThat(requestedLine.getId()).isEqualTo(id);
        assertThat(requestedLine.getName()).isEqualTo(line1.getName());
        assertThat(requestedLine.getColor()).isEqualTo(line1.getColor());
        assertThat(requestedLine.getExtraFare()).isEqualTo(line1.getExtraFare());
    }

    @DisplayName("존재하지 않는 id 값을 가진 노선 반환 시도")
    @Test
    void findLineNotFoundException() {
        // given

        // when
        Long id = -1L;

        // then
        assertThatThrownBy(() -> lineDao.find(id))
            .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @DisplayName("모든 노선 호출")
    @Test
    void findAll() {
        // given
        Line line1 = new Line("10호선", "붉은색", 3_000);
        lineDao.insert(line1);
        Line line2 = new Line("11호선", "노란색", 500);
        lineDao.insert(line2);

        // when
        List<Line> lines = lineDao.findAll();

        // then
        assertThat(lines.get(0).getName()).isEqualTo(line1.getName());
        assertThat(lines.get(0).getColor()).isEqualTo(line1.getColor());
        assertThat(lines.get(0).getExtraFare()).isEqualTo(line1.getExtraFare());
        assertThat(lines.get(1).getName()).isEqualTo(line2.getName());
        assertThat(lines.get(1).getColor()).isEqualTo(line2.getColor());
        assertThat(lines.get(1).getExtraFare()).isEqualTo(line2.getExtraFare());
    }

    @DisplayName("노선 업데이트")
    @Test
    void update() {
        // given
        Line line1 = new Line("11호선", "보라색", 0);
        Line saveLine = lineDao.insert(line1);
        Long id = saveLine.getId();
        String requestName = "분당선";
        String requestColor = "노란색";
        int requestExtraFare = 500;
        Line requestLine = new Line(requestName, requestColor, requestExtraFare);

        // when
        lineDao.update(id, requestLine);
        Line responseLine = lineDao.find(id);

        // then
        assertThat(responseLine.getName()).isEqualTo(requestName);
        assertThat(responseLine.getColor()).isEqualTo(requestColor);
        assertThat(responseLine.getExtraFare()).isEqualTo(requestExtraFare);
    }

    @DisplayName("노선 삭제")
    @Test
    void remove() {
        // given
        Line line1 = new Line("12호선", "분홍색", 0);
        Line saveLine = lineDao.insert(line1);
        long id = saveLine.getId();

        // when
        int number = lineDao.delete(id);

        // then
        assertThat(number).isEqualTo(1);
    }

    @DisplayName("존재하지 않는 id를 통한 노선 삭제 시도시 예외처리")
    @Test
    void removeNotFoundException() {
        // given
        long id = -1;

        // when
        int number = lineDao.delete(id);

        // then
        assertThat(number).isEqualTo(0);
    }
}