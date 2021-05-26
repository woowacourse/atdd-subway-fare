package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.farepolicy.FarePolicyFactory;
import wooteco.subway.station.domain.Station;

class SubwayPathTest {

    @DisplayName("요금 계산")
    @ParameterizedTest
    @CsvSource(value = {"10, 2150", "11, 2250", "15, 2250", "16, 2350", "50, 2950", "58, 3050"})
    void fare(int distance, int expectedFare) {
        SectionEdge sectionEdge = new SectionEdge(
            new Section(1L, neozalStation(), rootStation(), distance),
            new Line(1L, "백기선", "red", 900)
        );

        SubwayPath subwayPath = new SubwayPath(Collections.singletonList(sectionEdge), Arrays.asList(neozalStation(), rootStation()), new Fare(
            FarePolicyFactory.create()));

        assertThat(subwayPath.fare().intValue()).isEqualTo(expectedFare);
    }

    private Station neozalStation() {
        return new Station(1L, "너잘");
    }

    private Station rootStation() {
        return new Station(2L, "루트");
    }

}