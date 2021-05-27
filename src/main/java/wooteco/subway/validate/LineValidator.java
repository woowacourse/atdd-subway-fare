package wooteco.subway.validate;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import wooteco.subway.exception.LineInvalidBoundaryException;
import wooteco.subway.exception.UnsupportedSubwayCharacterException;
import wooteco.subway.line.dto.LineRequest;

import java.util.regex.Pattern;

public class LineValidator implements Validator {
    private static final String PATTERN = "^[a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$";

    @Override
    public boolean supports(Class<?> clazz) {
        return LineRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LineRequest lineRequest = (LineRequest) target;

        if (Pattern.matches(PATTERN, lineRequest.getName())) {
            throw new UnsupportedSubwayCharacterException();
        }

        if (lineRequest.getName().length() < 2 && lineRequest.getName().length() > 10) {
            throw new LineInvalidBoundaryException();
        }
    }
}
