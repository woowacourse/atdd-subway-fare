package wooteco.subway.line.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.assertj.core.api.Assertions.linesOf;
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
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.line.domain.Line;
import wooteco.subway.station.dto.StationRequest;

class LineRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("이름 입력값이 빈 값, null, 특수 문자, 영어, 글자수는 2자 미만이거나 10자 초과면 안 된다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"썬", "가나다라마바사아자차카타파하, %, )))"})
    void create(String name) {
        final LineRequest lineRequest = new LineRequest(name, "black", 1L, 2L, 10);
        final Set<ConstraintViolation<LineRequest>> violations = validator.validate(lineRequest);
        assertThat(violations).isNotEmpty();
    }

    @DisplayName("요금을 제외하고, 문자 입력값은 빈 값이면 안 되고 숫자 입력값은 양수여야 한다.")
    @ParameterizedTest
    @CsvSource(value = {"\":black:1:2:10", "제이온:\":0:2:10", "제이온:black:1:0:10",
    "제이온:black:1:0:1", "제이온:black:1:2:0"}, delimiter = ',')
    void create2(String input) {
        final String[] inputs = input.split(":");
        final LineRequest lineRequest = new LineRequest(inputs[0], inputs[1],
            Long.parseLong(inputs[2]), Long.parseLong(inputs[3]), Integer.parseInt(inputs[4]));
        final Set<ConstraintViolation<LineRequest>> violations = validator.validate(lineRequest);
        assertThat(violations).isNotEmpty();
    }
}