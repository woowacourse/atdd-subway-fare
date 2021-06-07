package wooteco.subway.web.dto.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class SubwayNameValidator implements ConstraintValidator<SubwayName, String> {

    @Override
    public void initialize(SubwayName contactNumber) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext cxt) {
        return name.matches("^[가-힣0-9]+$") && StringUtils.isNotBlank(name);
    }

}
