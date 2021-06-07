package wooteco.subway;

import io.restassured.RestAssured;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class AcceptanceTest {

    @LocalServerPort
    int port;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;

        List<String> sql = Arrays.asList(
            "delete from section",
            "delete from station",
            "delete from line",
            "delete from member",
            "ALTER TABLE section ALTER COLUMN id RESTART WITH 1",
            "ALTER TABLE station ALTER COLUMN id RESTART WITH 1",
            "ALTER TABLE line ALTER COLUMN id RESTART WITH 1",
            "ALTER TABLE member ALTER COLUMN id RESTART WITH 1"
        );

        sql.forEach(jdbcTemplate::update);
    }
}
