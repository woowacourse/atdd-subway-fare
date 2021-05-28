package wooteco.subway.validate;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import wooteco.subway.exception.AgeInvalidBoundaryException;
import wooteco.subway.exception.EmailInvalidBoundaryException;
import wooteco.subway.exception.PasswordInvalidBoundaryException;
import wooteco.subway.exception.UnsupportedCharacterException;
import wooteco.subway.member.dto.MemberRequest;

import java.util.regex.Pattern;

public class MemberValidator implements Validator {
    private static final String PATTERN = "/^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i";

    @Override
    public boolean supports(Class<?> clazz) {
        return MemberRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MemberRequest memberRequest = (MemberRequest) target;

        if (Pattern.matches(PATTERN, memberRequest.getEmail())) {
            throw new UnsupportedCharacterException();
        }

        if (memberRequest.getEmail().length() > 30) {
            throw new EmailInvalidBoundaryException();
        }

        if (memberRequest.getPassword().length() < 4 || memberRequest.getPassword().length() > 20) {
            throw new PasswordInvalidBoundaryException();
        }

        if (memberRequest.getAge() < 0 || memberRequest.getAge() > 200) {
            throw new AgeInvalidBoundaryException();
        }
    }
}
