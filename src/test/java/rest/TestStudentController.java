package rest;

import base.StudentMicroserviceRunner;
import base.entity.Group;
import base.entity.Student;
import base.entity.University;
import base.serviceDB.ServiceDatabase;
import base.web.config.SourceParameterWrapperStudent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest (classes = StudentMicroserviceRunner.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TestStudentController {

    private static final Logger log = LogManager.getLogger(TestStudentController.class.getName());

    @Resource
    private ServiceDatabase service;

    //@Value("${server.port}")
    @LocalServerPort
    String port;

    private RestTemplate restTemplate = new RestTemplate();

    private final String url =      "http://localhost:%s/";
    private final String getAll =   url+"student/all";
    private final String filtr =    url+"student/filtr?name=nam&sname=surnam";
    private final String filtr1 =   url+"student/filtr?name=name5&sname=surname5";
    private final String groupId =  url+"student/group?id=10";
    private final String post =     url+"student?id=10";
    private final String del =      url+"student/delete?name=name9&sname=surname9";

    @BeforeEach
    public void testBefore() {

        assertNotNull(service);
        service.clearTable();
    }

    @Test
    public void TestGetAll(){

        addStudent();
        List<Student> allStudent = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent);
        assertEquals(allStudent.size() , 10);
        log.trace(allStudent);
    }

    @Test
    public void TestFiltr(){

        addStudent();

        //http://localhost:8080/student/filtr?name=name1&sname=surname1
        List<Student> allStudent = restTemplate.getForObject(String.format(filtr,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent);
        assertEquals(allStudent.size() , 10);
        log.trace(allStudent);

        List<Student> allStudent1 = restTemplate.getForObject(String.format(filtr1,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent1);
        assertEquals(allStudent1.size(), 1);
        log.trace(allStudent1);
    }

    @Test
    public void TestGroupId(){

        addStudent();
        List<Student> allStudent = restTemplate.getForObject(String.format(groupId,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent);
        assertEquals(allStudent.size() , 10);
        log.trace(allStudent);

    }


    @Test
    public void TestAddNewStudent(){

        long id = 100L, groupIdn = 10;
        University un = new University(id,"Best university");

        Group gr = new Group(groupIdn,"group1");
        gr.setUniversity(un);
        un.getListGroup().add(gr);
        service.saveUniversity(un);

        for(long i = 0; i< 10; i++){
            Student s = new Student("name"+i,"surname"+i,new Date());
            ResponseEntity<Student> e = restTemplate.postForEntity(String.format(post,port),s,Student.class);
            assertEquals(e.getStatusCode(),  HttpStatus.CREATED);
            log.trace(e.getStatusCode());
        }
        List<Student> allStudent = restTemplate.getForObject(String.format(groupId,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent);
        assertEquals(allStudent.size(),10);
        log.trace(allStudent);
    }

    @Test
    public void TestDelete(){

        addStudent();
        restTemplate.delete(String.format(del,port));

        List<Student> allStudent = restTemplate.getForObject(String.format(groupId,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent);
        assertEquals(allStudent.size() , 9);
        log.trace(allStudent);
    }
    private void addStudent(){
        long id = 100L, groupIdn = 10;
        University un = new University(id,"Best university");

        Group gr = new Group(groupIdn,"group1");
        gr.setUniversity(un);

        for(long i = 0; i< 10; i++){
            Student s = new Student("name"+i,"surname"+i,new Date());
            s.setGroup(gr);
            gr.getListStudents().add(s);
        }
        un.getListGroup().add(gr);
        service.saveUniversity(un);
        List<Student> l = service.findStudentByGroupId(groupIdn);
        assertEquals(l.size() , 10);
    }



}
