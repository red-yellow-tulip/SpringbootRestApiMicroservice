import app.RedisApplicationRunner;
import app.config.RedisService;
import app.config.entity.Record;
import app.config.utils.SourceParameterWrapperRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = RedisApplicationRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestPersonRecordController {

    @Autowired
    protected RedisService redisService;

    private final String url =      "http://localhost:%s/person";
    private final String getAll =   url + "/all";
    private final String getId =    url + "/?id=%s";
    private final String post =     url + "/add";
    private final String del =      url + "/delete?id=%s";
    private final String delAll =   url + "/delete/all";

    @LocalServerPort
    protected String port;

    private TestRestTemplate restTemplate = new TestRestTemplate();

    @BeforeEach
    public void testBefore() {

        deleteAllPerson();

        List<Record> recordList = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperRecord.ListWrapper.class);
        assertEquals (0, recordList.size() );
    }

    @Test
    public void TestGetAll() {

        int countRecord = 100;
        for (int i = 0; i < countRecord; i++){
            Record p = new Record( "GetAll"+i, i);
            ResponseEntity<Record> personRecord = restTemplate.postForEntity(String.format(post,port), p, Record.class);
            assertEquals (personRecord.getStatusCode() , HttpStatus.CREATED);
        }
        List<Record> recordList = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperRecord.ListWrapper.class);
        assertEquals (countRecord, recordList.size() );
    }

    @Test
    public void TestDeleteAllById() {

        int countRecord = 300;
        for (int i = 0; i < countRecord; i++){
            Record p = new Record("deleteAll"+i, i);
            ResponseEntity<Record> personRecord = restTemplate.postForEntity(String.format(post,port), p, Record.class);
            assertEquals (personRecord.getStatusCode() , HttpStatus.CREATED);
        }

        List<Record> recordList = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperRecord.ListWrapper.class);
        recordList.parallelStream().forEach(e -> {
            restTemplate.delete(String.format(del,port,String.valueOf(e.getId())));
        });

        recordList = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperRecord.ListWrapper.class);
        assertEquals (0, recordList.size() );

    }

    @Test
    public void TestGetById() {

        int countRecord = 100;
        for (int i = 0; i < countRecord; i++){
            Record p = new Record("GetById"+i, i);
            ResponseEntity<Record> personRecord = restTemplate.postForEntity(String.format(post,port), p, Record.class);
            assertEquals (personRecord.getStatusCode() , HttpStatus.CREATED);
        }
        List<Record> recordList = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperRecord.ListWrapper.class);
        assertEquals (countRecord, recordList.size() );

        recordList.parallelStream().forEach(e -> {
            Record pr = restTemplate.getForObject(String.format(getId,port, String.valueOf(e.getId())), Record.class);

            assertNotNull (pr);
            assertNotNull (pr.getId());
            assertNotNull (pr.getName());
            assertNotNull (pr.getGrade());
            assertEquals (e.getId(), pr.getId());

            assertTrue (pr.getName().contains("GetById"));
        });
    }

    private void deleteAllPerson(){
        restTemplate.delete(String.format(delAll,port));
    }

}
