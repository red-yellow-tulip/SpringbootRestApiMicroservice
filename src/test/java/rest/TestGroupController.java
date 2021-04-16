package rest;

import base.StudentMicroserviceRunner;
import base.datasource.entity.Group;
import base.web.config.SourceParameterWrapperGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
public class TestGroupController  extends BaseTestHelper{

    private final String url =      "http://localhost:%s/";
    private final String getAll = url + "group/all";
    private final String filtr = url + "group/filtr?name=group2";
    private final String filtr1 = url + "group/filtr?name=group";
    private final String groupId = url + "group?id=51";
    private final String post = url + "group/add";
    private final String del = url + "group/delete?id=55";

    @BeforeEach
    public void testBefore() {
        assertNotNull(service);
        service.clearTable();
        service.createDemoData();
    }

    @Test
    public void TestGetAll() {

        long start = System.currentTimeMillis();
        List<Group> allGroup = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperGroup.ListWrapper.class);
        long executionTime = System.currentTimeMillis() - start;
        log.warn("getAll выполнен за " + executionTime + "мс" );

        assertEquals (allGroup.size() , 5);
        log.trace(allGroup);
    }

    @Test
    public void TestFiltr() {

        long start = System.currentTimeMillis();
        List<Group> allGroup = restTemplate.getForObject(String.format(filtr,port), SourceParameterWrapperGroup.ListWrapper.class);
        long executionTime = System.currentTimeMillis() - start;
        log.warn("filtr выполнен за " + executionTime + "мс" );

        assertEquals (allGroup.size() , 1);
        log.trace(allGroup);

        List<Group> allGroup1 =  restTemplate.getForObject(String.format(filtr1,port), SourceParameterWrapperGroup.ListWrapper.class);
        assertEquals (allGroup1.size() ,5);
        log.trace(allGroup1);
    }

    @Test
    public void TestGroupId() {

        long start = System.currentTimeMillis();
        Group group1 = restTemplate.getForObject(String.format(groupId,port), Group.class);
        long executionTime = System.currentTimeMillis() - start;
        log.warn("groupId выполнен за " + executionTime + "мс" );

        assertNotNull(group1);
        log.trace(group1);
    }

    @Test
    public void TestAddNewGroup() {

        long id = 100L, groupIdn = 55;

        Group gr = new Group(groupIdn, "group55");
        long start = System.currentTimeMillis();
        ResponseEntity<Group> e = restTemplate.postForEntity(String.format(post,port), gr, Group.class);
        long executionTime = System.currentTimeMillis() - start;
        log.warn("post выполнен за " + executionTime + "мс" );
        assertNotEquals(e.getStatusCode() , HttpStatus.FORBIDDEN);
        assertEquals(e.getStatusCode() , HttpStatus.CREATED);
        log.trace(e.getStatusCode());

        List<Group> allGroup1 = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperGroup.ListWrapper.class);
        assertEquals (allGroup1.size() , 6);
        log.trace(allGroup1);
    }

    @Test
    public void TestDelete() {

        long id = 100L, groupIdn = 55;
        Group gr = new Group(groupIdn, "group55");
        long start = System.currentTimeMillis();
        ResponseEntity<Group> e = restTemplate.postForEntity(String.format(post,port), gr, Group.class);
        long executionTime = System.currentTimeMillis() - start;
        log.warn("post выполнен за " + executionTime + "мс" );
        assertEquals (e.getStatusCode() , HttpStatus.CREATED);
        log.trace(e.getStatusCode());
        restTemplate.delete(String.format(del,port));

        List<Group> allGroup1 =  restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperGroup.ListWrapper.class);
        assertEquals (allGroup1.size() ,  5);
        log.trace(allGroup1);
    }
}
