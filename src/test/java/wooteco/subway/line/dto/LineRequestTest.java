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
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class LineRequestTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("역 형식에 맞는 요청")
    public void validInput() {
        LineRequest lineRequest = new LineRequest("2호선", "white", 1L, 2L, 19);
        Set<ConstraintViolation<LineRequest>> violations = validator.validate(lineRequest);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"123!", "2 호선", ".2호선"})
    @DisplayName("노선 이름 옳지 않은 요청")
    public void invalidName(String name) {
        LineRequest lineRequest = new LineRequest(name, "white", 1L, 2L, 19);
        Set<ConstraintViolation<LineRequest>> violations = validator.validate(lineRequest);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("노선 이름")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("노선 색상 옳지 않은 요청")
    public void invalidColor(String color) {
        LineRequest lineRequest = new LineRequest("name", color, 1L, 2L, 19);
        Set<ConstraintViolation<LineRequest>> violations = validator.validate(lineRequest);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("노선 색상")));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1})
    @DisplayName("노선 거리 옳지 않은 요청")
    public void invalidDistance(int distance) {
        LineRequest lineRequest = new LineRequest("name", "color", 1L, 2L, distance);
        Set<ConstraintViolation<LineRequest>> violations = validator.validate(lineRequest);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("구간 거리")));
    }

    @Test
    @DisplayName("상행/하행 역 옳지 않은 요청")
    public void invalidStation() {
        Long stationId = null;
        LineRequest lineRequest = new LineRequest("name", "color", stationId, stationId, 100);
        Set<ConstraintViolation<LineRequest>> violations = validator.validate(lineRequest);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("역")));
    }
}