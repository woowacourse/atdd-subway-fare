package wooteco.subway.member.dto;

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
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberRequestTest {
    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("형식에 맞는 요청")
    public void validInput() {
        MemberRequest memberRequest = new MemberRequest("asdf@asdf.com", "asdf", 20);
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(memberRequest);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("이메일 공백")
    public void emptyEmail() {
        MemberRequest memberRequest = new MemberRequest("", "asdf", 20);
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(memberRequest);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("INVALID_EMAIL")));
    }

    @Test
    @DisplayName("이메일 null")
    public void nullEmail() {
        MemberRequest memberRequest = new MemberRequest(null, "asdf", 20);
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(memberRequest);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("INVALID_EMAIL")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"asdfasdf.com", "asdf@asdfcom", "asd!f@asdf.com", "a@sdf@asdf.com"})
    @DisplayName("이메일 아이디에 특수문자")
    public void invalidEmailWithNotString(String value) {
        MemberRequest memberRequest = new MemberRequest(value, "asdf", 20);
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(memberRequest);
        System.out.println(violations);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("INVALID_EMAIL")));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    @DisplayName("비밀번호 공백")
    public void emptyPassword(String password) {
        MemberRequest memberRequest = new MemberRequest("asdf@asdf.com", password, 20);
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(memberRequest);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("INVALID_PASSWORD")));
    }

    @Test
    @DisplayName("옳지 않은 나이")
    public void invalidAge() {
        MemberRequest memberRequest = new MemberRequest("asdf@asdf.com", "password", 0);
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(memberRequest);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("INVALID_AGE")));
    }

    @Test
    @DisplayName("나이 null")
    public void nullAge() {
        MemberRequest memberRequest = new MemberRequest("asdf@asdf.com", "password", null);
        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(memberRequest);
        assertTrue(violations.stream()
            .anyMatch(violation -> violation.getMessage().contains("INVALID_AGE")));
    }
}