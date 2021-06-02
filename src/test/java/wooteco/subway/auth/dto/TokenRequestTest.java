package wooteco.subway.auth.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class TokenRequestTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("형식에 맞는 요청")
    public void validInput() {
        TokenRequest tokenRequest = new TokenRequest("asdf@asdf.com", "asdf");
        Set<ConstraintViolation<TokenRequest>> violations = validator.validate(tokenRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("이메일 공백")
    public void emptyEmail() {
        TokenRequest tokenRequest = new TokenRequest("", "asdf");
        Set<ConstraintViolation<TokenRequest>> violations = validator.validate(tokenRequest);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("이메일은 공백이 아닙니다.")));
    }

    @Test
    @DisplayName("이메일 null")
    public void nullEmail() {
        TokenRequest tokenRequest = new TokenRequest(null, "asdf");
        Set<ConstraintViolation<TokenRequest>> violations = validator.validate(tokenRequest);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("이메일은 공백이 아닙니다.")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"asdfasdf.com", "asdf@asdfcom", "asd!f@asdf.com", "a@sdf@asdf.com"})
    @DisplayName("이메일 아이디에 특수문자")
    public void invalidEmailWithNotString(String value) {
        TokenRequest tokenRequest = new TokenRequest(value, "asdf");
        Set<ConstraintViolation<TokenRequest>> violations = validator.validate(tokenRequest);
        System.out.println(violations);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("이메일에는 특수문자가 포함될 수 없습니다.")));
    }

    @Test
    @DisplayName("비밀번호 공백")
    public void emptyPassword() {
        TokenRequest tokenRequest = new TokenRequest("asdf@asdf.com", "");
        Set<ConstraintViolation<TokenRequest>> violations = validator.validate(tokenRequest);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("비밀번호는 공백일 수 없습니다.")));
    }

    @Test
    @DisplayName("비밀번호 null")
    public void nullPassword() {
        TokenRequest tokenRequest = new TokenRequest("asdf@asdf.com", null);
        Set<ConstraintViolation<TokenRequest>> violations = validator.validate(tokenRequest);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("비밀번호는 공백일 수 없습니다.")));
    }
}