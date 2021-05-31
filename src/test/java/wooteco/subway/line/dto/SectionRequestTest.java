package wooteco.subway.line.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SectionRequestTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("상행역, 하행역, 거리는 모두 1 이상의 값이어야 한다.")
    @ParameterizedTest
    @CsvSource(value = {"0:2:10", "1:0:10", "1:2:0"}, delimiter = ',')
    void create(String input) {
        final String[] inputs = input.split(":");
        final SectionRequest sectionRequest = new SectionRequest(
            Long.parseLong(inputs[0]), Long.parseLong(inputs[1]), Integer.parseInt(inputs[2]));
        final Set<ConstraintViolation<SectionRequest>> violations = validator.validate(sectionRequest);
        assertThat(violations).isNotEmpty();
    }
}