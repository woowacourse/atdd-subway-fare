package wooteco.subway.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("로그인 멤버 도메인 테스트")
class LoginMemberTest {

    @DisplayName("비로그인 사용자 동등성 테스트")
    @Test
    void anonymous() {
        // given
        LoginMember anonymous = LoginMember.anonymous();

        // when
        boolean isSame = anonymous.equals(LoginMember.anonymous());

        // then
        assertTrue(isSame);
        assertThat(anonymous.getId()).isEqualTo(null);
        assertThat(anonymous.getEmail()).isEqualTo(null);
        assertThat(anonymous.getAge()).isEqualTo(0);
    }

    @DisplayName("비로그인 사용자인지 확인")
    @Test
    void isAnonymous() {
        // given
        LoginMember anonymous = LoginMember.anonymous();

        // when
        boolean isAnonymous = anonymous.isAnonymous();

        // then
        assertTrue(isAnonymous);
    }
}