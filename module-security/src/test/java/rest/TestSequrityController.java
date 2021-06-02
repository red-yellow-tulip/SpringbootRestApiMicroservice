package rest;

import base.DataMicroserviceRunner;
import base.datasource.DatabaseService;
import base.datasource.entity.Data;
import base.utils.SourceParameterWrapperData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

//@ActiveProfiles("test.h2")
@SpringBootTest(classes = DataMicroserviceRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestSequrityController {

    @Autowired
    private DatabaseService databaseService;

    @LocalServerPort
    protected String port;

    protected TestRestTemplate restTemplateAdmin = new TestRestTemplate("ADMIN", "pswd");
    protected TestRestTemplate restTemplateUser = new TestRestTemplate("USER1", "pswd");


    private final String url =      "http://localhost:%s/";
    private final String getAll =   url+"data/all";
    private final String getId =    url+"data/id/1";

    @BeforeEach
    public void testBefore() {
        databaseService.createDemoData();
    }

    @Test
    public void TestGetAll(){

        List<Data> dataList = restTemplateAdmin.getForObject(String.format(getAll,port), SourceParameterWrapperData.ListWrapper.class);
        List<Data> dataListUser = restTemplateUser.getForObject(String.format(getAll,port), SourceParameterWrapperData.ListWrapper.class);

        isCheck(dataList,"d1","d2");
        isCheck(dataListUser, "d1", "d2");


        Data data = restTemplateAdmin.getForObject(String.format(getId,port), Data.class);
        assertNotNull(data);
        assertEquals(data.getD1() , "d3");
        assertEquals(data.getD2() , "d4");

    }

    private void isCheck(List<Data> dataList, String s1, String s2) {
        assertNotNull(dataList);
        assertEquals(dataList.size() , 1);
        assertEquals(dataList.get(0).getD1(), s1);
        assertEquals(dataList.get(0).getD2(), s2);
    }
}
