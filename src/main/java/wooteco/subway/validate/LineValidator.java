package wooteco.subway.validate;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import wooteco.subway.exception.LineInvalidBoundaryException;
import wooteco.subway.exception.UnsupportedSubwayCharacterException;
import wooteco.subway.line.dto.LineRequest;

public class LineValidator implements Validator {
    private static final String SPACE = " ";
    private static final int WORD_LENGTH = 1;

    @Override
    public boolean supports(Class<?> clazz) {
        return LineRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LineRequest lineRequest = (LineRequest) target;

        if (hasSpace(lineRequest.getName())) {
            throw new UnsupportedSubwayCharacterException();
        }

        if (lineRequest.getName().length() < 2 || lineRequest.getName().length() > 10) {
            throw new LineInvalidBoundaryException();
        }
    }

    private boolean hasSpace(String value) {
        return value.split(SPACE).length != WORD_LENGTH;
    }
}
