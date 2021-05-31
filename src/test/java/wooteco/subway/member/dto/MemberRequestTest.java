package wooteco.subway.member.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MemberRequestTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @DisplayName("이메일 입력값은 이메일 형식을 갖춰야 하며, 빈 문자열, null, 공백이 아닌 4글자 이상 20글자 이하여야 한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", "123", "1@1", "123213213213213211233@email.com"})
    void create_email(String email) {
        final MemberRequest memberRequest = new MemberRequest(email, "test", 20);
        final Set<ConstraintViolation<MemberRequest>> violations = validator.validate(memberRequest);
        assertThat(violations).isNotEmpty();
    }

    @DisplayName("패스워드 입력값은 빈 문자열, null, 공백이 아닌 4글자 이상 20글자 이하여야 한다.")
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"", "123", "가나@email.com", "1@1", "123213213213213211233@email.com"})
    void create_password(String password) {
        final MemberRequest memberRequest = new MemberRequest("email@email.com", password, 20);
        final Set<ConstraintViolation<MemberRequest>> violations = validator.validate(memberRequest);
        assertThat(violations).isNotEmpty();
    }

    @DisplayName("나이 입력값은 양수여야 한다.")
    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void create_age(int age) {
        final MemberRequest memberRequest = new MemberRequest("email@email.com", "test" , age);
        final Set<ConstraintViolation<MemberRequest>> violations = validator.validate(memberRequest);
        assertThat(violations).isNotEmpty();
    }
}