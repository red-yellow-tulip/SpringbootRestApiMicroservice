package rest;

import base.StudentMicroserviceRunner;
import base.datasource.entity.Group;
import base.datasource.entity.Student;
import base.datasource.entity.University;
import base.datasource.DatabaseService;
import base.web.config.SourceParameterWrapperGroup;
import base.web.config.SourceParameterWrapperStudent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest (classes = StudentMicroserviceRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TestGroupController {

    private static final Logger log = LogManager.getLogger(TestGroupController.class.getName());

    //@Value("${server.port}")
    @LocalServerPort
    String port;

    TestRestTemplate restTemplate = new TestRestTemplate("ADMIN","pswd");
    @Resource
    private DatabaseService service;

    @Autowired
    private  StudentMicroserviceRunner studentMicroserviceRunner;

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

        List<Group> allGroup = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperGroup.ListWrapper.class);
        assertEquals (allGroup.size() , 5);
        log.trace(allGroup);
    }

    @Test
    public void TestFiltr() {

        List<Group> allGroup = restTemplate.getForObject(String.format(filtr,port), SourceParameterWrapperGroup.ListWrapper.class);
        assertEquals (allGroup.size() , 1);
        log.trace(allGroup);

        List<Group> allGroup1 =  restTemplate.getForObject(String.format(filtr1,port), SourceParameterWrapperGroup.ListWrapper.class);
        assertEquals (allGroup1.size() ,5);
        log.trace(allGroup1);
    }

    @Test
    public void TestGroupId() {

        Group group1 = restTemplate.getForObject(String.format(groupId,port), Group.class);
        assertNotNull(group1);
        log.trace(group1);
    }

    @Test
    public void TestAddNewGroup() {

        long id = 100L, groupIdn = 55;

        Group gr = new Group(groupIdn, "group55");
        ResponseEntity<Group> e = restTemplate.postForEntity(String.format(post,port), gr, Group.class);
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
        ResponseEntity<Group> e = restTemplate.postForEntity(String.format(post,port), gr, Group.class);
        assertEquals (e.getStatusCode() , HttpStatus.CREATED);
        log.trace(e.getStatusCode());
        restTemplate.delete(String.format(del,port));

        List<Group> allGroup1 =  restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperGroup.ListWrapper.class);
        assertEquals (allGroup1.size() ,  5);
        log.trace(allGroup1);
    }
}
