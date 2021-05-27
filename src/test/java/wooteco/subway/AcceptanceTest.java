package wooteco.subway;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
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
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    public void tearDown() {
        jdbcTemplate.execute("ALTER TABLE STATION SET REFERENTIAL_INTEGRITY FALSE;");
        jdbcTemplate.execute("TRUNCATE TABLE STATION");
        jdbcTemplate.execute("ALTER TABLE STATION SET REFERENTIAL_INTEGRITY FALSE;");
        jdbcTemplate.execute("TRUNCATE TABLE LINE");
        jdbcTemplate.execute("TRUNCATE TABLE SECTION");
        jdbcTemplate.execute("TRUNCATE TABLE MEMBER");

        jdbcTemplate.execute("ALTER TABLE STATION ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE LINE ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE SECTION ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.execute("ALTER TABLE MEMBER ALTER COLUMN id RESTART WITH 1");
    }
}
