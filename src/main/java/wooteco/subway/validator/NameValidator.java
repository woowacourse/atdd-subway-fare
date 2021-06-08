package wooteco.subway.validator;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class NameValidator implements ConstraintValidator<SubwayName, String> {

    public static final Pattern NAME_PATTERN = Pattern.compile("^[가-힣0-9]+$");

    @Override
    public void initialize(SubwayName subwayNameValidator) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return NAME_PATTERN.matcher(name).matches() && StringUtils.isNotBlank(name);
    }
}
