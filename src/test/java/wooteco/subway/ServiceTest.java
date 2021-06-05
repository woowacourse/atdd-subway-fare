package wooteco.subway;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

@SpringBootTest
@Sql(value = "classpath:truncateAll.sql", executionPhase = AFTER_TEST_METHOD)
@ActiveProfiles("test")
public class ServiceTest {
}
