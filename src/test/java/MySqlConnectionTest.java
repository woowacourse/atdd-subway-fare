//import org.junit.jupiter.api.Test;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//
//public class MySqlConnectionTest {
//    private static final String DRIVER = "com.mysql.jdbc.Driver";
//    private static final String URL  = "jdbc:mysql://127.0.0.1:3306/spring_subway?serverTimezone=UTC&characterEncoding=UTF-8";
//    private static final String USER = "ecsimsw";
//    private static final String PASSWORD = "root";
//
//    @Test
//    public void testConnection() throws ClassNotFoundException, SQLException {
//        Class.forName(DRIVER);
//
//        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
//            assertThat(connection).isNotNull();
//        }
//    }
//}
