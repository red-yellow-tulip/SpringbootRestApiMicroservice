import app.KafkaMicroserviceRunner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = KafkaMicroserviceRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestKafkaController {

    @BeforeEach
    public void testBefore() {


    }

    @Test
    public void TestGetAll() {


    }


}
