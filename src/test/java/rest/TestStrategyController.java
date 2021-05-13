package rest;

import base.strategy.utils.EStrategyNames;
import base.strategy.utils.dto.Request;
import base.strategy.utils.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import rest.helper.BaseTestHelper;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class TestStrategyController extends BaseTestHelper {

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
