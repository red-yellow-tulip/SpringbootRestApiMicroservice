import app.ApplicationRunner;
import app.client.service.DataClient;
import app.utils.dto.RequestDTO;
import app.utils.dto.ResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(classes = {ApplicationRunner.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestControllerMVC {

    @LocalServerPort
    private String port;

    @Autowired
    private WebClient webClient;

    @Autowired
    private DataClient dataClient;

    private String endpoint = "http://localhost:%s/data/";

    @Test
    public void TestGetByIdBlock() {

        String id = "1";
        ResponseDTO response = dataClient.getDataByIdBlock(String.format(endpoint + id, port));

        assertNotNull(response);
        assertEquals(response.getStrEng(), id);
    }

    @Test
    public void TestGetByIdAsync() throws InterruptedException {

        String id = "99";
        AtomicReference<String> s = new AtomicReference<>("");
        Flux<ResponseDTO> response = dataClient.getDataByIdAsync(String.format(endpoint +"as/"+ id, port));
        response
                .log()
                .subscribe( x -> {
                    assertNotNull(x);
                    assertEquals(x.getStrEng(), "999");
                    }
        );
        TimeUnit.SECONDS.sleep(1);
    }

    @Test
    public void TestPostByIdBlock() {

        String id = "20";
        String url = String.format(endpoint + id, port);

        RequestDTO requestDTO = RequestDTO.builder()
                .date(LocalDate.now())
                .strEng("Eng20")
                .strRus("Rus20")
                .val(BigDecimal.TEN)
                .build();
        ResponseDTO response = dataClient.postDataByIdBlock(url, requestDTO);

        assertNotNull(response);
        assertEquals(response.getStrRus(), "RUS20");
    }

    @Test
    public void TestGetAllAsync() throws InterruptedException {

        int count = 25;
        String urlGetAll = String.format(endpoint + "all/" + count, port);

        AtomicLong counter = new AtomicLong();

        Flux<ResponseDTO> response = dataClient.getAllDataAsync(urlGetAll);

        response.subscribe(r -> {
            counter.incrementAndGet();
            assertEquals(r.getStrRus(), String.valueOf(counter));
            System.out.println(r);
        });
        TimeUnit.SECONDS.sleep(3);

        assertEquals(counter.intValue(), count);
    }

    @Test
    public void TestGetAllAsyncClient() throws InterruptedException {

        int count = 25;
        String urlGetAll = String.format(endpoint + "all/" + count, port);
        AtomicLong counter = new AtomicLong();

        webClient
                .get()
                .uri(urlGetAll)
               .accept(MediaType.TEXT_EVENT_STREAM)
                .exchangeToFlux(response -> {  //exchangeToFlux   - получить тело + заголовок  <--->  retrieve - получить тело
                            if (response.statusCode().equals(HttpStatus.OK)) {
                                return response.bodyToFlux(ResponseDTO.class);
                            } else {
                                return Flux.error(new IllegalArgumentException());
                            }
                        }
                        /*r -> r.bodyToFlux(ResponseDTO.class)*/
                )
                //.bodyToFlux(ResponseDTO.class)
                .doOnNext(r -> {
                    counter.incrementAndGet();
                })
                .subscribe(
                        response -> System.out.println("GET: " + response));

        TimeUnit.SECONDS.sleep(3);
        assertEquals(counter.intValue(), count);
    }

}
