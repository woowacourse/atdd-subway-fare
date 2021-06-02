package wooteco.subway.path.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import wooteco.subway.auth.domain.AnonymousUser;
import wooteco.subway.auth.domain.LoginUser;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.domain.SectionEdge;
import wooteco.subway.path.domain.SubwayPath;
import wooteco.subway.station.domain.Station;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class FareCalculatorTest {
    private static final int TOTAL_DISTANCE = 50;
    private static final int ORIGINAL_FARE = 2050;
    public static final int EXTRA_FARE_OF_LINE = 105;
    private static final int DEDUCTIBLE_FARE = 350;

    @Autowired
    private FareCalculator fareCalculator;

    @MockBean
    private PathFinder pathFinder;

    @BeforeEach
    void setUp() {
        final Station 시작역 = new Station("시작역");
        final Station 도착역 = new Station("도착역");
        final Section section = new Section(1L, 시작역, 도착역, TOTAL_DISTANCE);
        final Line 테스트선 = new Line(1L, "테스트선", "테스트색", EXTRA_FARE_OF_LINE, new Sections(Arrays.asList(section)));

        final SectionEdge sectionEdge = new SectionEdge(section, 테스트선);
        final SubwayPath subwayPath = new SubwayPath(Arrays.asList(sectionEdge), Arrays.asList(시작역, 도착역));

        Mockito.when(pathFinder.findPath(null, null, null)).thenReturn(subwayPath);
    }

    @Test
    @DisplayName("익명의 사용자는 아무런 할인 혜택을 받을 수 없다")
    void calculateFareAnonymous() {
        final int fare = fareCalculator.calculateFare(new AnonymousUser(),
                pathFinder.findPath(null, null, null));

        assertThat(fare).isEqualTo(ORIGINAL_FARE + EXTRA_FARE_OF_LINE);
    }

    @Test
    @DisplayName("어른 사용자는 아무런 할인 혜택을 받을 수 없다.")
    void calculateFareAdult() {
        final int fare = fareCalculator.calculateFare(new LoginUser(1L, "child@ren.com", 45),
                pathFinder.findPath(null, null, null));

        assertThat(fare).isEqualTo(ORIGINAL_FARE + EXTRA_FARE_OF_LINE);
    }

    @Test
    @DisplayName("어린이 사용자는 공제금을 제외한 50% 할인을 받을 수 있다.")
    void calculateFareChildren() {
        final int fare = fareCalculator.calculateFare(new LoginUser(1L, "child@ren.com", 8),
                pathFinder.findPath(null, null, null));

        final double expectedFare = (ORIGINAL_FARE + EXTRA_FARE_OF_LINE - DEDUCTIBLE_FARE) * 0.5 + DEDUCTIBLE_FARE;
        assertThat(fare).isEqualTo((int)expectedFare);
    }

    @Test
    @DisplayName("청소년 사용자는 공제금을 제외한 20% 할인을 받을 수 있다.")
    void calculateFareTeenager() {
        final int fare = fareCalculator.calculateFare(new LoginUser(1L, "child@ren.com", 16),
                pathFinder.findPath(null, null, null));

        final double expectedFare = (ORIGINAL_FARE + EXTRA_FARE_OF_LINE - DEDUCTIBLE_FARE) * 0.8 + DEDUCTIBLE_FARE;
        assertThat(fare).isEqualTo((int)expectedFare);
    }
}