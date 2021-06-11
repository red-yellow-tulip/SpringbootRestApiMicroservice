import app.ApplicationRunner;
import app.client.service.DataClient;
import app.controller.dto.ResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ApplicationRunner.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestController {

    @LocalServerPort
    private String port;

    @Autowired
    private DataClient dataClient;

    private String endpoint = "http://localhost:%s/data/";

    @Test
    public void TestGetBlock() {

        String id = "1";
        ResponseDTO response = dataClient.getDataByIdBlock(String.format(endpoint+id,port));

        assertNotNull(response);
        assertEquals(response.getStrEng(), id);
    }

    @Test
    public void TestGet() {

        String id = "2";
        Mono<ResponseDTO> response = dataClient.getDataByIdAsync(String.format(endpoint+id,port));

        assertTrue(response.blockOptional().isPresent());
        assertEquals(response.blockOptional().get().getStrEng(), id);
    }



}
