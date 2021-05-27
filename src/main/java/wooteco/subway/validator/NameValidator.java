package wooteco.subway.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class NameValidator implements ConstraintValidator<SubwayName, String> {

    @Override
    public void initialize(SubwayName subwayNameValidator) {
    }

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return name.matches("^[가-힣0-9]+$") && StringUtils.isNotBlank(name);
    }
}
