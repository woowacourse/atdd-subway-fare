package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.member.domain.LoginMember;

@DisplayName("요금정책에 따라 요금을 계산한다.")
class FareCalculatorTest {

    private final FareCalculator calculator = new FareCalculator();
    private final int shortDistance = 5;
    private final int mediumDistance = 15;
    private final int longDistance = 58;

    @DisplayName("거리에 따른 요금 정책을 적용한다.")
    @Test
    void calculateLogoutFare() {
        LoginMember member = new LoginMember();
        List<SectionEdge> edges = Collections
            .singletonList(new SectionEdge(new Section(), new Line("2호선", "yellow", 0)));

        assertAll(
            () -> assertThat(calculator.calculate(member, shortDistance, edges)).isEqualTo(1250),
            () -> assertThat(calculator.calculate(member, mediumDistance, edges)).isEqualTo(1450),
            () -> assertThat(calculator.calculate(member, longDistance, edges)).isEqualTo(2250)
        );
    }

    @DisplayName("노선에 따른 요금 정책을 적용한다.")
    @Test
    void calculateLineOverFare() {
        LoginMember member = new LoginMember();
        SectionEdge edge1 = new SectionEdge(new Section(), new Line("신분당선", "yellow", 900));
        SectionEdge edge2 = new SectionEdge(new Section(), new Line("2호선", "yellow", 0));
        List<SectionEdge> edges = Arrays.asList(edge1, edge2);

        assertAll(
            () -> assertThat(calculator.calculate(member, shortDistance, edges)).isEqualTo(2150),
            () -> assertThat(calculator.calculate(member, mediumDistance, edges)).isEqualTo(2350),
            () -> assertThat(calculator.calculate(member, longDistance, edges)).isEqualTo(3150)
        );
    }

    @DisplayName("나이에 따른 요금 정책을 적용한다.")
    @Test
    void calculateLoginFare() {
        LoginMember childMember = new LoginMember(1L, "email@email.com", 9);
        LoginMember teenMember = new LoginMember(2L, "email@email.com", 16);
        LoginMember adultMember = new LoginMember(3L, "email@email.com", 25);
        List<SectionEdge> edges = Collections
            .singletonList(new SectionEdge(new Section(), new Line("2호선", "yellow", 0)));

        assertAll(
            () -> assertThat(calculator.calculate(childMember, shortDistance, edges))
                .isEqualTo(800),
            () -> assertThat(calculator.calculate(teenMember, shortDistance, edges))
                .isEqualTo(1070),
            () -> assertThat(calculator.calculate(adultMember, shortDistance, edges))
                .isEqualTo(1250)
        );
    }
}
