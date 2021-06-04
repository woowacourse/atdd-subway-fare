package wooteco.subway.path.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.member.domain.GuestUser;
import wooteco.subway.member.domain.LoginUser;
import wooteco.subway.path.dto.PathResponse;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PathServiceTest {

    @Autowired
    private PathService pathService;

    @DisplayName("경로 조회 확인")
    @Test
    void name() {
        PathResponse pathResponse = pathService.findPath(1L, 2L, new GuestUser());

        assertThat(pathResponse.getDistance()).isEqualTo(10);
        assertThat(pathResponse.getFare()).isEqualTo(1250);
    }
}
