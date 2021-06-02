package wooteco.subway.web.dto.validator;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SubwayNameValidator implements ConstraintValidator<SubwayName, String> {

    @Override
    public void initialize(SubwayName subwayName) {
    }

    @Override
    public boolean isValid(String name,
                           ConstraintValidatorContext cxt) {
        return name.matches("^[가-힣0-9]+$") && StringUtils.isNotEmpty(name);
    }
}
