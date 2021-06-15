import app.ApplicationRunner;
import app.client.service.DataClient;
import app.controller.dto.RequestDTO;
import app.controller.dto.ResponseDTO;
import app.controller.mapper.Mapper;
import app.controller.utils.Request;
import app.controller.utils.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    public void TestGetByIdBlock() {

        String id = "1";
        ResponseDTO response = dataClient.getDataByIdBlock(String.format(endpoint+id,port));

        assertNotNull(response);
        assertEquals(response.getStrEng(), id);
    }

    @Test
    public void TestPostByIdBlock() {

        String id = "20";
        String url = String.format(endpoint+id,port);

        RequestDTO requestDTO= RequestDTO.builder()
                .date(LocalDate.now())
                .strEng("Eng20")
                .strRus("Rus20")
                .val(BigDecimal.TEN)
                .build();
        ResponseDTO response = dataClient.postDataByIdBlock(url,requestDTO);

        assertNotNull(response);
        assertEquals(response.getStrRus(), "RUS20");
    }

    @Test
    public void TestGetAllAsync() throws InterruptedException {


        int count = 50;
        String urlGetAll = String.format(endpoint+"all/"+count,port);

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
}
