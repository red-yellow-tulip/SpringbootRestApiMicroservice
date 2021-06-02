import app.CalcApplicationRunner;
import app.strategy.utils.EStrategyNames;
import app.strategy.utils.dto.Request;
import app.strategy.utils.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = CalcApplicationRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestStrategyController  {

    @LocalServerPort
    protected String port;

    protected TestRestTemplate restTemplate = new TestRestTemplate();

    private final String url = "http://localhost:%s/calc/byStrategy/";

    @Test
    public void TestPLUS() {

        Request request = Request.builder()
                .op1(24)
                .op2(6)
                .build();
        String endPoint = String.format(url+EStrategyNames.PLUS,port);
        Response response = restTemplate.postForObject(endPoint,request, Response.class );

        assertEquals (30L, response.getResult());
    }

    @Test
    public void TestMINUS() {

        Request request = Request.builder()
                .op1(24)
                .op2(6)
                .build();
        String endPoint = String.format(url+EStrategyNames.MINUS,port);
        Response response = restTemplate.postForObject(endPoint,request, Response.class );

        assertEquals (18L, response.getResult());
    }

    @Test
    public void TestDEVIDE() {

        Request request = Request.builder()
                .op1(24)
                .op2(6)
                .build();
        String endPoint = String.format(url+EStrategyNames.DEVIDE,port);
        Response response = restTemplate.postForObject(endPoint,request, Response.class );

        assertEquals (4L, response.getResult());
    }

    @Test
    public void TestMULTI() {

        Request request = Request.builder()
                .op1(4)
                .op2(6)
                .build();
        String endPoint = String.format(url+EStrategyNames.MULTI,port);
        Response response = restTemplate.postForObject(endPoint,request, Response.class );

        assertEquals (24L, response.getResult());
    }


}
