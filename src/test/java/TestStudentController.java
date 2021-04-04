import base.StudentMicroserviceRunner;
import base.entity.Group;
import base.entity.Student;
import base.entity.University;
import base.serviceDB.ServiceDatabase;
import base.web.config.SourceParameterWrapperStudent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
@ContextConfiguration (classes = StudentMicroserviceRunner.class)
@ActiveProfiles("test")
public class TestStudentController {

    private RestTemplate restTemplate = new RestTemplate();
    @Value("${server.port}")
    String port;

    @Resource
    private ServiceDatabase service;

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
        assert(allStudent.size() == 10);
        System.out.println(allStudent);
    }

    @Test
    public void TestFiltr(){

        addStudent();

        //http://localhost:8080/student/filtr?name=name1&sname=surname1
        List<Student> allStudent = restTemplate.getForObject(String.format(filtr,port), SourceParameterWrapperStudent.ListWrapper.class);
        assert(allStudent.size() == 10);
        System.out.println(allStudent);

        List<Student> allStudent1 = restTemplate.getForObject(String.format(filtr1,port), SourceParameterWrapperStudent.ListWrapper.class);
        assert(allStudent1.size() == 1);
        System.out.println(allStudent1);
    }

    @Test
    public void TestGroupId(){

        addStudent();
        List<Student> allStudent = restTemplate.getForObject(String.format(groupId,port), SourceParameterWrapperStudent.ListWrapper.class);
        assert(allStudent.size() == 10);
        System.out.println(allStudent);

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
            assert(e.getStatusCode() ==  HttpStatus.CREATED);
            System.out.println(e.getStatusCode());

        }

        List<Student> allStudent = restTemplate.getForObject(String.format(groupId,port), SourceParameterWrapperStudent.ListWrapper.class);
        assert(allStudent.size() == 10);
        System.out.println(allStudent);
    }

    @Test
    public void TestDelete(){

        addStudent();
        restTemplate.delete(String.format(del,port));

        List<Student> allStudent = restTemplate.getForObject(String.format(groupId,port), SourceParameterWrapperStudent.ListWrapper.class);
        assert(allStudent.size() == 9);
        System.out.println(allStudent);
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
        assert(l.size() == 10);

    }



}
