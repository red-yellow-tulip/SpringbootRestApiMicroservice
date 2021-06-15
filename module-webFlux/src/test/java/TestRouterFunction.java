import app.ApplicationRunner;
import app.client.service.DataClient;
import app.utils.dto.RequestDTO;
import app.utils.dto.ResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(classes = {ApplicationRunner.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestRouterFunction {

    @LocalServerPort
    private String port;

    @Autowired
    private WebClient webClient;

    @Autowired
    private DataClient dataClient;

    private String endpoint = "http://localhost:%s/route-data/";

    @Test
    public void TestGetByIdBlock() {

        String id = "1";
        ResponseDTO response = dataClient.getDataByIdBlock(String.format(endpoint + id, port));

        assertNotNull(response);
        assertEquals(response.getStrEng(), id);
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

        int count = 50;
        String urlGetAll = String.format(endpoint + "all/" + count, port);

        AtomicLong counter = new AtomicLong();

        Flux<ResponseDTO> response = dataClient.getAllDataAsync(urlGetAll);

        response.subscribe(r -> {
            counter.incrementAndGet();
            assertEquals(r.getStrRus(), String.valueOf(counter));
            System.out.println(r);
        });
        TimeUnit.SECONDS.sleep(1);

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
                .accept(APPLICATION_JSON)
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

        TimeUnit.SECONDS.sleep(1);

        assertEquals(counter.intValue(), count);

    }

}
