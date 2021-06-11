import app.ApplicationRunner;
import app.client.service.DataClient;
import app.controller.dto.ResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = {ApplicationRunner.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestController {

    @LocalServerPort
    private String port;

    @Autowired
    private WebClient webClient;

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
    public void TestGetAsync() {

        String id = "2";
        Mono<ResponseDTO> response = dataClient.getDataByIdAsync(String.format(endpoint+id,port));
        response.subscribe(r -> {
            assertEquals(r.getStrRus(), id);
        });

        assertTrue(response.blockOptional().isPresent());
        assertEquals(response.blockOptional().get().getStrEng(), id);
    }

    @Test
    public void TestGetAllAsync() throws InterruptedException {

        String urlGetAll = "all";
        AtomicLong counter = new AtomicLong();

        Flux<ResponseDTO> response = dataClient.getAllDataAsync(String.format(endpoint+urlGetAll,port));

       /* webClient
                .get()
                .uri(String.format(endpoint+urlGetAll,port)).exchange().subscribe(f -> {

            System.out.println(f);
        });*/

        response.subscribe(r -> {
            counter.incrementAndGet();
            assertEquals(r.getStrRus(), String.valueOf(counter));
             System.out.println(r);
        });

        TimeUnit.SECONDS.sleep(2);


        //assertEquals(counter, 100);
    }




}
