package wooteco.subway.validate;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import wooteco.subway.exception.StationInvalidBoundaryException;
import wooteco.subway.exception.UnsupportedSubwayCharacterException;
import wooteco.subway.station.dto.StationRequest;

import java.util.regex.Pattern;

public class StationValidator implements Validator {
    private static final String PATTERN = "[a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]";

    @Override
    public boolean supports(Class<?> clazz) {
        return StationRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        StationRequest stationRequest = (StationRequest) target;

        if (Pattern.matches(PATTERN, stationRequest.getName())) {
            throw new UnsupportedSubwayCharacterException();
        }

        if (stationRequest.getName().length() < 2 && stationRequest.getName().length() > 20) {
            throw new StationInvalidBoundaryException();
        }
    }
}
