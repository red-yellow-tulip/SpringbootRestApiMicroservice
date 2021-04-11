package rest;

import base.StudentMicroserviceRunner;
import base.entity.Group;
import base.entity.Student;
import base.entity.University;
import base.logging.LoggerAspect;
import base.serviceDB.ServiceDatabase;
import base.web.config.SourceParameterWrapperGroup;
import base.web.config.SourceParameterWrapperStudent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest (classes = StudentMicroserviceRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TestGroupController {

    private static final Logger log = LogManager.getLogger(TestGroupController.class.getName());

    //@Value("${server.port}")
    @LocalServerPort
    String port;

    TestRestTemplate restTemplate = new TestRestTemplate();  // new TestRestTemplate("user", "passwd");
    @Resource
    private ServiceDatabase service;

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
    }

    @Test
    public void TestGetAll() {

        addGroup();
        List<Group> allGroup = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperGroup.ListWrapper.class);
        assert (allGroup.size() == 5);
        log.trace(allGroup);
    }

    @Test
    public void TestFiltr() {

        addGroup();
        List<Group> allGroup = restTemplate.getForObject(String.format(filtr,port), SourceParameterWrapperGroup.ListWrapper.class);
        assertEquals (allGroup.size() , 1);
        log.trace(allGroup);

        List<Group> allGroup1 =  restTemplate.getForObject(String.format(filtr1,port), SourceParameterWrapperGroup.ListWrapper.class);
        assertEquals (allGroup1.size() ,5);
        log.trace(allGroup1);
    }

    @Test
    public void TestGroupId() {

        addGroup();
        Group group1 = restTemplate.getForObject(String.format(groupId,port), Group.class);
        assertNotNull(group1);
        log.trace(group1);
    }

    @Test
    public void TestAddNewGroup() {

        addGroup();
        long id = 100L, groupIdn = 55;

        Group gr = new Group(groupIdn, "group55");
        ResponseEntity<Group> e = restTemplate.postForEntity(String.format(post,port), gr, Group.class);
        assertEquals(e.getStatusCode() , HttpStatus.CREATED);
        log.trace(e.getStatusCode());

        List<Group> allGroup1 = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperGroup.ListWrapper.class);
        assertEquals (allGroup1.size() , 6);
        log.trace(allGroup1);
    }

    @Test
    public void TestDelete() {

        addGroup();
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


    private void addGroup() {

        long id = 100L, groupIdn = 10;
        University un = new University(id, "Best university");

        for (long j = 0; j < 5; j++) {
            Group gr = new Group(groupIdn + j, "group" + j);
            gr.setUniversity(un);
            gr.setGroupId(50 + j);

            for (long i = j * 10; i < j * 10 + 10; i++) {
                Student s = new Student("name" + i, "surname" + i, new Date());
                s.setGroup(gr);
                gr.getListStudents().add(s);
            }
            un.getListGroup().add(gr);
        }
        service.saveUniversity(un);

        List<Student> allStudent = restTemplate.getForObject(String.format(url,port) + "student/all", SourceParameterWrapperStudent.ListWrapper.class);
        assertEquals (allStudent.size() , 50);
        log.trace(allStudent);
    }
}
