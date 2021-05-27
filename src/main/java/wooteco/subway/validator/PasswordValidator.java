package wooteco.subway.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<SubwayPassword, String> {

    private int min;
    private int max;

    @Override
    public void initialize(SubwayPassword subwayPassword) {
        min = subwayPassword.min();
        max = subwayPassword.max();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return password.matches("^[0-9A-Za-z@$!%*?&]+$")
            && min <= password.length()
            && max >= password.length();
    }
}
