package wooteco.subway.validator;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<SubwayPassword, String> {

    public static final Pattern PASSWORD_PATTERN = Pattern.compile("^[0-9A-Za-z@$!%*?&]+$");
    private int min;
    private int max;

    @Override
    public void initialize(SubwayPassword subwayPassword) {
        min = subwayPassword.min();
        max = subwayPassword.max();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        return PASSWORD_PATTERN.matcher(password).matches()
            && min <= password.length()
            && max >= password.length();
    }
}
