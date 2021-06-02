package wooteco.subway.line.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class SectionRequestTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("구간 형식에 맞는 요청")
    public void validInput() {
        SectionRequest sectionRequest = new SectionRequest(1L, 2L, 100);
        Set<ConstraintViolation<SectionRequest>> violations = validator.validate(sectionRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("상행/하행역 옳지 않은 요청")
    public void invalidStations() {
        Long stationId = null;
        SectionRequest sectionRequest = new SectionRequest(stationId, stationId, 100);
        Set<ConstraintViolation<SectionRequest>> violations = validator.validate(sectionRequest);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("역은 공백일 수 없습니다.")));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("구간 거리 옳지 않은 요청")
    public void invalidDistance(int distance) {
        SectionRequest sectionRequest = new SectionRequest(1L, 2L, distance);
        Set<ConstraintViolation<SectionRequest>> violations = validator.validate(sectionRequest);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("구간 거리")));
    }
}