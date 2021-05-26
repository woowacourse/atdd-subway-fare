package wooteco.subway.station.dto;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class StationRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("입력값이 빈 값, null, 2자 미만, 10자 초과면 안 된다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"썬", "가나다라마바사아자차카타파하"})
    void create(String name) {
        final StationRequest stationRequest = new StationRequest(name);
        final Set<ConstraintViolation<StationRequest>> violations = validator.validate(stationRequest);
        assertThat(violations).isNotEmpty();
    }
}