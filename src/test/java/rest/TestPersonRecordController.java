package rest;

import base.datasource.memDb.entity.PersonRecord;
import base.web.config.SourceParameterWrapperPersonRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import rest.helper.BaseTestHelper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class TestPersonRecordController extends BaseTestHelper {

    private final String url =      "http://localhost:%s/person";
    private final String getAll =   url + "/all";
    private final String getId =    url + "/?id=%s";
    private final String post =     url + "/add";
    private final String del =      url + "/delete?id=%s";
    private final String delAll =   url + "/delete/all";

    @BeforeEach
    public void testBefore() {

        deleteAllPerson();

        List<PersonRecord> personRecordList = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperPersonRecord.ListWrapper.class);
        assertEquals (0, personRecordList.size() );
    }

    @Test
    public void TestGetAll() {

        int countRecord = 100;
        for (int i = 0; i < countRecord; i++){
            PersonRecord p = new PersonRecord( "GetAll"+i, i);
            ResponseEntity<PersonRecord> personRecord = restTemplate.postForEntity(String.format(post,port), p, PersonRecord.class);
            assertEquals (personRecord.getStatusCode() , HttpStatus.CREATED);
        }
        List<PersonRecord> personRecordList = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperPersonRecord.ListWrapper.class);
        assertEquals (countRecord, personRecordList.size() );
    }

    @Test
    public void TestDeleteAllById() {

        int countRecord = 300;
        for (int i = 0; i < countRecord; i++){
            PersonRecord p = new PersonRecord("deleteAll"+i, i);
            ResponseEntity<PersonRecord> personRecord = restTemplate.postForEntity(String.format(post,port), p, PersonRecord.class);
            assertEquals (personRecord.getStatusCode() , HttpStatus.CREATED);
        }

        List<PersonRecord> personRecordList = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperPersonRecord.ListWrapper.class);
        personRecordList.parallelStream().forEach( e -> {
            restTemplate.delete(String.format(del,port,String.valueOf(e.getId())));
        });

        personRecordList = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperPersonRecord.ListWrapper.class);
        assertEquals (0, personRecordList.size() );

    }

    @Test
    public void TestGetById() {

        int countRecord = 100;
        for (int i = 0; i < countRecord; i++){
            PersonRecord p = new PersonRecord("GetById"+i, i);
            ResponseEntity<PersonRecord> personRecord = restTemplate.postForEntity(String.format(post,port), p, PersonRecord.class);
            assertEquals (personRecord.getStatusCode() , HttpStatus.CREATED);
        }
        List<PersonRecord> personRecordList = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperPersonRecord.ListWrapper.class);
        assertEquals (countRecord, personRecordList.size() );

        personRecordList.parallelStream().forEach(e -> {
            PersonRecord pr = restTemplate.getForObject(String.format(getId,port, String.valueOf(e.getId())), PersonRecord.class);

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
