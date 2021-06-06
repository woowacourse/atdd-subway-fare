package wooteco.subway.station.dto;

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

class StationRequestTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("역 형식에 맞는 요청")
    public void validInput() {
        StationRequest stationRequest = new StationRequest("name");
        Set<ConstraintViolation<StationRequest>> violations = validator.validate(stationRequest);
        assertTrue(violations.isEmpty());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"123!", "2 번 역", ".2역"})
    @DisplayName("역 이름 옳지 않은 요청")
    public void invalidName(String name) {
        StationRequest stationRequest = new StationRequest(name);
        Set<ConstraintViolation<StationRequest>> violations = validator.validate(stationRequest);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("INVALID_NAME")));
    }
}