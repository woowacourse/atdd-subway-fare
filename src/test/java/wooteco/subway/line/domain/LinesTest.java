package wooteco.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.DuplicateLineNameException;

class LinesTest {

    @Test
    @DisplayName("중복된 이름을 가진 지하철 노선이 있는지 검증")
    void validateDuplicateName() {
        // given
        List<Line> lineList = Arrays.asList(
            new Line("1호선", "bg-blue-100", 0),
            new Line("2호선", "bg-red-300", 0),
            new Line("3호선", "bg-orange-500", 0)
        );

        Lines lines = new Lines(lineList);

        Line duplicateNameLine = new Line("1호선", "bg-black-000", 0);

        // when

        // then
        assertThatThrownBy(() -> lines.validateDuplicate(duplicateNameLine))
            .isInstanceOf(DuplicateLineNameException.class);
    }

    @Test
    @DisplayName("이름 기준 오름차순 정렬 확인(숫자 우선, 그 후 한글)")
    void sortedByLineName() {
        // given
        List<Line> lineList = Arrays.asList(
            new Line("후후선", "bg-blue-100", 0),
            new Line("다라선", "bg-blue-100", 20),
            new Line("가나선", "bg-blue-100", 30),
            new Line("2호선", "bg-blue-100", 40),
            new Line("1호선", "bg-blue-100", 500)
        );

        Lines lines = new Lines(lineList);

        // when
        List<Line> sortedLines = lines.sortedByName();

        // then
        assertThat(sortedLines.get(0).getName()).isEqualTo("1호선");
        assertThat(sortedLines.get(1).getName()).isEqualTo("2호선");
        assertThat(sortedLines.get(2).getName()).isEqualTo("가나선");
        assertThat(sortedLines.get(3).getName()).isEqualTo("다라선");
        assertThat(sortedLines.get(4).getName()).isEqualTo("후후선");
    }
}