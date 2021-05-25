package wooteco.subway.member.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.subway.exception.duplication.EmailDuplicatedException;
import wooteco.subway.member.dao.MemberDao;
import wooteco.subway.member.dto.MemberRequest;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberDao memberDao;

    @DisplayName("회원가입시 이메일 중복을 체크한다.")
    @Test
    void cannotRegister() {
        given(memberDao.countByEmail("test@naver.com")).willReturn(1);

        MemberRequest memberRequest = new MemberRequest("test@naver.com", "123", 19);
        assertThatThrownBy(() -> memberService.createMember(memberRequest))
                .isInstanceOf(EmailDuplicatedException.class)
                .hasMessage("중복된 이메일입니다.");
    }
}
