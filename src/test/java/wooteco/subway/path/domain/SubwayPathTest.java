package wooteco.subway.path.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.LineFixture.*;
import static wooteco.subway.line.SectionFixture.*;

@DisplayName("지하철 경로 도메인 테스트")
public class SubwayPathTest {

    @DisplayName("환승 시 노선 최대 추가 요금 확인")
    @Test
    void calculateFare() {
        // given
        List<SectionEdge> sectionEdges = Arrays.asList(
                new SectionEdge(
                        왕십리_잠실_거리22,
                        이호선
                ),
                new SectionEdge(
                        잠실_강남_거리25,
                        신분당선
                ),
                new SectionEdge(
                        강남_구의_거리10,
                        사호선
                )
        );

        SubwayPath subwayPath = new SubwayPath(sectionEdges, Collections.emptyList());

        // when
        int fare = subwayPath.extraFare();

        // then
        assertThat(fare).isEqualTo(2000);
    }
}
