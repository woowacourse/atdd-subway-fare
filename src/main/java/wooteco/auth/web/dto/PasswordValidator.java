package wooteco.auth.web.dto;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {
    private int maxSize;
    private int minSize;

    @Override
    public void initialize(Password constraintAnnotation) {
        this.maxSize = constraintAnnotation.maxSize();
        this.minSize = constraintAnnotation.minSize();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches("^[0-9A-Za-z@$!%*?&]+$")
                && minSize <= value.length()
                && maxSize >= value.length()
                && StringUtils.isNotBlank(value);
    }
}