import app.KafkaMicroserviceRunner;
import app.controller.Consumer;
import app.controller.dto.Address;
import app.controller.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.context.EmbeddedKafka;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@EnableKafka
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
@SpringBootTest(classes = KafkaMicroserviceRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestKafkaController {

    @Autowired
    private Consumer consumer;

    @LocalServerPort
    protected String port;

    private final String url =      "http://localhost:%s";
    private final String post =     url + "/msg?topic=topic2&msgId=5";

    private TestRestTemplate restTemplate = new TestRestTemplate();


    @Test
    public void TestSendMsg() {

        UserDto userDto = UserDto.builder()
                .age(50L)
                .name("name")
                .address(Address.builder()
                        .city("")
                        .country("")
                        .flatNumber(1L)
                        .homeNumber(2L)
                        .build())
                .build();

        ResponseEntity<UserDto> personRecord = restTemplate.postForEntity(String.format(post,port), userDto, UserDto.class);
        assertEquals (personRecord.getStatusCode() , HttpStatus.OK);

        UserDto secondUserDto = consumer.getPrevUserDto();
        assertNotNull(secondUserDto);
        assertEquals(51L,secondUserDto.getAge());
        assertEquals(1L,consumer.getCounter());

    }


}
