package wooteco.subway.validate;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import wooteco.subway.exception.StationInvalidBoundaryException;
import wooteco.subway.exception.UnsupportedSubwayCharacterException;
import wooteco.subway.station.dto.StationRequest;

public class StationValidator implements Validator {
    private static final String SPACE = " ";
    private static final int WORD_LENGTH = 1;

    @Override
    public boolean supports(Class<?> clazz) {
        return StationRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        StationRequest stationRequest = (StationRequest) target;

        if (hasSpace(stationRequest.getName())) {
            throw new UnsupportedSubwayCharacterException();
        }

        if (stationRequest.getName().length() < 2 || stationRequest.getName().length() > 20) {
            throw new StationInvalidBoundaryException();
        }
    }

    private boolean hasSpace(String value) {
        return value.split(SPACE).length != WORD_LENGTH;
    }
}
