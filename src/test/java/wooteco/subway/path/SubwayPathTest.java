package wooteco.subway.path;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.path.domain.SectionEdge;
import wooteco.subway.path.domain.SubwayPath;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.line.LineFixture.*;
import static wooteco.subway.line.SectionFixture.*;

public class SubwayPathTest {
    @DisplayName("요금 계산 - 50km 초과시")
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
                        이호선
                ),
                new SectionEdge(
                        강남_구의_거리10,
                        이호선
                )
        );
        SubwayPath subwayPath = new SubwayPath(sectionEdges, Collections.emptyList());

        // when
        int fare = subwayPath.calculateFare();

        // then
        assertThat(fare).isEqualTo(2150);
    }

    @DisplayName("요금 계산 - 10km ~ 50km")
    @Test
    void calculateFare2() {
        // given
        List<SectionEdge> sectionEdges = Arrays.asList(
                new SectionEdge(
                        왕십리_잠실_거리22,
                        이호선
                ),
                new SectionEdge(
                        잠실_강남_거리25,
                        이호선
                )
        );
        SubwayPath subwayPath = new SubwayPath(sectionEdges, Collections.emptyList());

        // when
        int fare = subwayPath.calculateFare();

        // then
        assertThat(fare).isEqualTo(2050);
    }

    @DisplayName("요금 계산 - 10km 이내")
    @Test
    void calculateFare3() {
        // given
        List<SectionEdge> sectionEdges = Collections.singletonList(
                new SectionEdge(
                        강남_구의_거리10,
                        이호선
                )
        );
        SubwayPath subwayPath = new SubwayPath(sectionEdges, Collections.emptyList());

        // when
        int fare = subwayPath.calculateFare();

        // then
        assertThat(fare).isEqualTo(1250);
    }

    @DisplayName("노선 환승 시 추가가요금 용 확인")
    @Test
    void transferFare() {
        // given
        List<SectionEdge> sectionEdges = Arrays.asList(
                new SectionEdge(
                        왕십리_잠실_거리22,
                        신분당선
                ),
                new SectionEdge(
                        잠실_강남_거리25,
                        이호선
                ),
                new SectionEdge(
                        강남_구의_거리10,
                        사호선
                )
        );
        SubwayPath subwayPath = new SubwayPath(sectionEdges, Collections.emptyList());

        // when
        int fare = subwayPath.calculateFare();

        // then
        assertThat(fare).isEqualTo(4150);
    }
}
