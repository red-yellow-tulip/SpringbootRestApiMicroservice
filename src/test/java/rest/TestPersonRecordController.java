package rest;

import base.datasource.memDb.entity.PersonRecord;
import base.datasource.sqlDb.entity.Group;
import base.web.config.SourceParameterWrapperGroup;
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
    private final String post =     url + "/add";


    @Test
    public void TestGetAll() {

        int countRecord = 10;
        for (int i = 0; i < countRecord; i++){
            PersonRecord p = new PersonRecord("name"+i, i);
            ResponseEntity<PersonRecord> personRecord = restTemplate.postForEntity(String.format(post,port), p, PersonRecord.class);
            assertEquals (personRecord.getStatusCode() , HttpStatus.CREATED);
        }

        List<PersonRecord> personRecordList = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperPersonRecord.ListWrapper.class);

        //assertEquals (countRecord, personRecordList.size() );
    }

}
