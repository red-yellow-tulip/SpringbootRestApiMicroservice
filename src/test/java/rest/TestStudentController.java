package rest;

import base.datasource.entity.Group;
import base.datasource.entity.Student;
import base.datasource.entity.University;
import base.web.config.SourceParameterWrapperStudent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import rest.helper.BaseTestHelper;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
public class TestStudentController extends BaseTestHelper {

    private final String url =      "http://localhost:%s/";
    private final String getAll =   url+"student/all";
    private final String filtr =    url+"student/filtr?name=nam&sname=surnam";
    private final String filtr1 =   url+"student/filtr?name=name5&sname=surname5";
    private final String groupId =  url+"student/group?id=54";
    private final String post =     url+"student?id=54";
    private final String del =      url+"student/delete?name=name40&sname=surname40";

    @BeforeEach
    public void testBefore() {
        assertNotNull(service);
        service.clearTable();
        service.createDemoData(5,10);
    }

    @Test
    public void TestGetAll(){

        List<Student> allStudent = restTemplate.getForObject(String.format(getAll,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent);
        assertEquals(allStudent.size() , 50);
        loggerService.log().trace(allStudent);
    }

    @Test
    public void TestFiltr(){

        //http://localhost:8080/student/filtr?name=name1&sname=surname1
        List<Student> allStudent = restTemplate.getForObject(String.format(filtr,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent);
        assertEquals(allStudent.size() , 50);
        loggerService.log().trace(allStudent);

        List<Student> allStudent1 = restTemplate.getForObject(String.format(filtr1,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent1);
        assertEquals(allStudent1.size(), 1);
        loggerService.log().trace(allStudent1);
    }

    @Test
    public void TestGroupId(){

        List<Student> allStudent = restTemplate.getForObject(String.format(groupId,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent);
        assertEquals(allStudent.size() , 10);
        loggerService.log().trace(allStudent);
    }


    @Test
    public void TestAddNewStudent(){

        long id = 101L, groupIdn = 10;
        University un = new University(id,"Best university");

        Group gr = new Group(groupIdn,"group1");
        gr.setUniversity(un);
        un.getListGroup().add(gr);
        service.saveUniversity(un);

        for(long i = 0; i< 10; i++){
            Student s = new Student("name"+60+i,"surname"+60+i,new Date());
            ResponseEntity<Student> e = restTemplate.postForEntity(String.format(post,port),s,Student.class);
            assertEquals(e.getStatusCode(),  HttpStatus.CREATED);
            loggerService.log().trace(e.getStatusCode());
        }
        List<Student> allStudent = restTemplate.getForObject(String.format(groupId,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent);
        assertEquals(allStudent.size(),20);
        loggerService.log().trace(allStudent);
    }

    @Test
    public void TestDelete(){

        restTemplate.delete(String.format(del,port));

        List<Student> allStudent = restTemplate.getForObject(String.format(groupId,port), SourceParameterWrapperStudent.ListWrapper.class);
        assertNotNull(allStudent);
        assertEquals(allStudent.size() , 9);
        loggerService.log().trace(allStudent);
    }
}
