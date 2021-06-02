package wooteco.auth.web.dto.validator;

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
    public boolean isValid(String password,
                           ConstraintValidatorContext cxt) {
        return password.matches("^[0-9A-Za-z@$!%*?&]+$") && minSize <= password.length() && maxSize >= password.length();
    }
}