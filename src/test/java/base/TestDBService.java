package base;

import base.datasource.entity.Group;
import base.datasource.entity.Student;
import base.datasource.entity.University;
import base.datasource.DatabaseService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration(classes = StudentMicroserviceRunner.class)
@ActiveProfiles("test")
public class TestDBService {

    private static final Logger log = LogManager.getLogger(TestDBService.class.getName());

    @Resource
    private DatabaseService service;

    @BeforeEach
    public void testBefore() {
        assertNotNull(service);
        service.clearTable();
    }

    @Test
    public void findUniversityTest(){

        long id = 100L;

        University un = new University(id,"Best university");
        University un2 = service.saveUniversity(un);
        assertNotNull (un2 );
        assertNotNull (un2.getId());
        log.info(un2);

        un = service.findUniversityById(id);
        assertNotNull (un);
        assertNotNull (un.getId() );
        log.info(un);
    }

    @Test
    public void findGroupIdTest2(){

        long id = 100L;
        University un = new University(id,"Best university");
        un= service.saveUniversity(un);

        Group gr = new Group(10L,"group1");
        gr.setUniversity(un);
        un.getListGroup().add(gr);
        service.saveUniversity(un);

        List<Group> lg =  service.findListGroupByUniversityId(id);
        assertEquals (lg.size() , 1);
    }

    @Test
    public void findGroupTest(){

        long id = 100L, groupId = 10;
        University un = new University(id,"Best university");

        Group gr = new Group(groupId,"demo_group1");
        gr.setUniversity(un);

        for(int i = 0; i< 10; i++){
            Student s = new Student("demo_name"+i,"demo_surname"+i,new Date());
            s.setGroup(gr);
            gr.getListStudents().add(s);
        }

        un.getListGroup().add(gr);
        service.saveUniversity(un);
        List<Student> l = service.findStudentByGroupId(groupId);

        log.info(l);
        assertEquals (l.size() ,  10);
        assertNotNull (l.get(5) );
    }

    @Test
    public void findStudentByGroupIdTest4(){

        long id = 100L, groupId = 10;
        University un = new University(id,"Best university");

        Group gr = new Group(groupId,"group1");
        gr.setUniversity(un);

        for(long i = 0; i< 10; i++){
            Student s = new Student("name"+i,"surname"+i,new Date());
            s.setGroup(gr);
            gr.getListStudents().add(s);
        }

        un.getListGroup().add(gr);
        service.saveUniversity(un);
        List<Student> l = service.findStudentByGroupId(groupId);

        Student s0 = new Student("name11","surname11",new Date());
        Student s00 = service.addStudentByGroupId(s0,groupId);
        assertNotNull (s00 );

        Student s001 = service.getStudentByNameAndSurname("name11","surname11");
        assertNotNull (s001 );
        assertNotEquals (s001.getGroupId() , 11L);

        l = service.findStudentByGroupId(groupId);
        assert (l.size() == 11);

        service.deleteStudent("name11","surname11");
        l = service.findStudentByGroupId(groupId);

        log.info(l);
        assert (l.size() == 10);
    }

    @Test
    public void createTest() {

        service.clearTable();
        long id = 100L, groupId = 1000;
        University un = new University(id, "Best university");

        for (char i = 'A'; i < 'Z'; i++) {
            Group gr = new Group(groupId + i, "group" +i);
            gr.setUniversity(un);

            for (int j = 1; j <= 10; j++) {
                log.info(i+":"+j);
                Student s = new Student(("name" + i) +j, ("surname" +  i)+j, new Date());
                s.setGroup(gr);
                gr.getListStudents().add(s);
            }
            un.getListGroup().add(gr);
        }
        service.saveUniversity(un);
    }
}
