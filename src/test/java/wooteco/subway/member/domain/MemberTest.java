package wooteco.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.member.application.MemberException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberTest {

    String validEmail = "valid";
    String validPassword = "valid";
    int validAge = 10;

    @DisplayName("회원을 생성한다.")
    @Test
    public void createMember() {
        assertThat(new Member(validEmail, validPassword, validAge)).isNotNull();
    }

    @DisplayName("유효하지 않은 회원 생성을 검증한다.")
    @Test
    public void createMemberWithInvalidInfo() {
        assertThatThrownBy(() -> new Member("", validPassword, validAge)).isInstanceOf(MemberException.class);
        assertThatThrownBy(() -> new Member(validEmail, "", validAge)).isInstanceOf(MemberException.class);
        assertThatThrownBy(() -> new Member(validEmail, validPassword, -1)).isInstanceOf(MemberException.class);
    }
}